package vn.dh_shop.service;

import jakarta.servlet.http.HttpServletRequest;
import vn.dh_shop.dto.payment.PaymentRequestDTO;
import vn.dh_shop.dto.payment.PaymentResponseDTO;

import java.util.Map;

public interface PaymentService {
    PaymentResponseDTO createVNpayPaymentUrl (PaymentRequestDTO request);
    String createVNpayReturnUrl(HttpServletRequest request);
    Map<String,String> handleIpnUrl(HttpServletRequest request);
}
