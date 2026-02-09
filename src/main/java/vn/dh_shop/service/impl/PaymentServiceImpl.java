package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.dh_shop.config.VnpayConfig;
import vn.dh_shop.dto.payment.PaymentRequestDTO;
import vn.dh_shop.dto.payment.PaymentResponseDTO;
import vn.dh_shop.entity.Order;
import vn.dh_shop.entity.Payment;
import vn.dh_shop.entity.enums.OrderStatus;
import vn.dh_shop.entity.enums.PaymentMethod;
import vn.dh_shop.entity.enums.PaymentStatus;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.OrderRepository;
import vn.dh_shop.repository.PaymentRepository;
import vn.dh_shop.security.util.SecurityUtils;
import vn.dh_shop.service.PaymentService;
import vn.dh_shop.util.VnpayUtil;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final SecurityUtils securityUtils;
    private final PaymentRepository paymentRepository;
    private final VnpayConfig vnpayConfig;
    private final VnpayUtil vnpayUtil;

    @Override
    @Transactional
    public PaymentResponseDTO createVNpayPaymentUrl(PaymentRequestDTO request) {

        Long userId = securityUtils.getUserId();
        if (userId == null)
            throw new BadRequestException("Không tìm thấy user");

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy order"));

        if (!order.getUser().getId().equals(userId))
            throw new BadRequestException("Order không hợp lệ");

        if (order.getStatus() != OrderStatus.PENDING)
            throw new BadRequestException("Đơn hàng không ở trạng thái thanh toán");

        // STEP 2: create payment INIT
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(PaymentMethod.VNPAY);
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(PaymentStatus.INIT);
        payment = paymentRepository.save(payment);

        // STEP 3: build VNPay params
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = vnpayConfig.getTmnCode();
        String vnp_Amount = String.valueOf(payment.getAmount() * 100);
        String vnp_CurrCode = "VND";
        String vnp_IpAddr = "0.0.0.0";
        String vnp_Locale = "vn";
        String vnp_OrderInfo = "Thanh toan don hang:" + payment.getOrder().getId();
        String vnp_OrderType = "other";
        String vnp_ReturnUrl = vnpayConfig.getReturnUrl();
        String vnp_TxnRef = payment.getId().toString();

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);


        String redirectUrl = vnpayUtil.buildVNpayPaymentUrl(vnp_Params);

        payment.setStatus(PaymentStatus.REDIRECTED);
        payment = paymentRepository.save(payment);

        return PaymentResponseDTO.builder()
                .orderId(order.getId())
                .paymentStatus(payment.getStatus())
                .method(PaymentMethod.VNPAY)
                .redirectUrl(redirectUrl)
                .message("VNPAY_REDIRECT")
                .orderStatus(order.getStatus())
                .build();
    }

    @Override
    public String createVNpayReturnUrl(HttpServletRequest request) {
        //Begin process return from VNPAY
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }
//        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        String vnp_SecureHash = fields.get("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = vnpayUtil.hashAllFields(fields);

//      Verify Signature
        if (!signValue.equals(vnp_SecureHash)) {
            throw new BadRequestException("INVALID_VNPAY_SIGNATURE");
        }
        // 5. PARSE DATA
        String responseCode = fields.get("vnp_ResponseCode");
        String transactionStatus = fields.get("vnp_TransactionStatus");
        String txnRef = fields.get("vnp_TxnRef"); // paymentId

        Long paymentId;
        try {
            paymentId = Long.parseLong(txnRef);
        } catch (Exception e) {
            throw new BadRequestException("INVALID_PAYMENT_ID");
        }
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        Long orderId = payment.getOrder().getId();
        boolean isSuccess = "00".equals(responseCode) && "00".equals(transactionStatus);

        // 8. REDIRECT FRONTEND
        if (isSuccess) {
            return "http://localhost:5173/payment/success?orderId=" + orderId;
        } else {
            return "http://localhost:5173/payment/fail?orderId=" + orderId;
        }
    }

    @Override
    @Transactional
    public Map<String,String> handleIpnUrl(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                log.info("fieldName: {} ----- fieldValue: {}", fieldName, fieldValue);
                fields.put(fieldName, fieldValue);
            }
        }
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = vnpayUtil.hashAllFields(fields);
        if (!signValue.equals(vnp_SecureHash)) {
            throw new BadRequestException("INVALID_VNPAY_SIGNATURE");
        }
        // 5. PARSE DATA
        String responseCode = fields.get("vnp_ResponseCode");
        String transactionStatus = fields.get("vnp_TransactionStatus");
        String txnRef = fields.get("vnp_TxnRef"); // paymentId
        Long vnp_Amount = Long.parseLong(fields.get("vnp_Amount"));
        String RspCode;
        String Message;
        // CHECK DATABASE
        Long paymentId;
        try {
            paymentId = Long.parseLong(txnRef);
        } catch (Exception e) {
            throw new BadRequestException("INVALID_PAYMENT_ID");
        }

//        Check responseCode & transactionStatus
        if (paymentRepository.existsById(paymentId)) {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
//        Check amount
            Long amount = payment.getAmount() * 100;
//      Check checkOrderStatus
            Order order = payment.getOrder();
            OrderStatus orderStatus = payment.getOrder().getStatus();
            log.info("amount --------: {}", amount);
            log.info("vnp_Amount -------: {}", vnp_Amount);
            if (amount.equals(vnp_Amount)) {
                if (orderStatus.equals(OrderStatus.PENDING)) {
                    if ("00".equals(fields.get("vnp_ResponseCode")) && "00".equals(transactionStatus)) {
                        //Here Code update PaymnentStatus = 1 into your Database
                        payment.setStatus(PaymentStatus.SUCCESS);
                        order.setStatus(OrderStatus.PAID);
                    } else {
                        payment.setStatus(PaymentStatus.FAILED);
                        order.setStatus(OrderStatus.PENDING);
                        // Here Code update PaymnentStatus = 2 into your Database
                    }
                    paymentRepository.save(payment);
                    orderRepository.save(order);
                    RspCode = "00";
                    Message = "Confirm Success";
                } else {
                    RspCode = "02";
                    Message = "Order already confirmed";
                }
            } else {
                RspCode = "04";
                Message = "Invalid Amount";
            }
        } else {
            RspCode = "01";
            Message = "Order not Found";
        }
        Map<String,String> result = new HashMap<>();
        result.put("RspCode",RspCode);
        result.put("Message",Message);
        return result;
}


}
