package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
public PaymentResponseDTO payWithVnpay(PaymentRequestDTO request) {

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
    String orderType = "orther";
    Long amount = payment.getAmount() * 100;
    String vnp_BankCode = "NCB";
    String vnp_OrderInfo = "Thanh toan don hang:"+payment.getOrder().getId();
    String vnp_IpAddr = "0.0.0.0";
    String vnp_TxnRef = payment.getId() + LocalDateTime.now().toString();
    String vnp_TmnCode = vnpayConfig.getTmnCode();

    Map<String, String> vnp_Params = new HashMap<>();
    vnp_Params.put("vnp_Version", vnp_Version);
    vnp_Params.put("vnp_Command", vnp_Command);
    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    vnp_Params.put("vnp_Amount", String.valueOf(amount));
    vnp_Params.put("vnp_CurrCode", "VND");
    vnp_Params.put("vnp_BankCode", vnp_BankCode);
    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
    vnp_Params.put("vnp_OrderType", orderType);
    vnp_Params.put("vnp_Locale", "vn");
    vnp_Params.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
    cld.add(Calendar.MINUTE, 15);
    String vnp_ExpireDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    //Build data to hash and querystring
    List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
    Collections.sort(fieldNames);

    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();

    Iterator<String> itr = fieldNames.iterator();
    while (itr.hasNext()) {
        String fieldName = (String) itr.next();
        String fieldValue = (String) vnp_Params.get(fieldName);
        if ((fieldValue != null) && (!fieldValue.isEmpty())) {
            //Build hash data
            hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            //Build query
            query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                    .append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            if (itr.hasNext()) {
                query.append('&');
                hashData.append('&');
            }
        }
    }

    String vnp_SecureHash = VnpayUtil.hmacSHA512(vnpayConfig.getHashSecret(), hashData.toString());
    query.append("&vnp_SecureHash=").append(vnp_SecureHash);

    String redirectUrl = vnpayConfig.getPayUrl()+"?"+query;



    return PaymentResponseDTO.builder()
            .orderId(order.getId())
            .paymentStatus(payment.getStatus())
            .method(PaymentMethod.VNPAY)
            .redirectUrl(redirectUrl)
            .message("VNPAY_REDIRECT")
            .orderStatus(order.getStatus())
            .build();
}

}
