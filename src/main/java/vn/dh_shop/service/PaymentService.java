package vn.dh_shop.service;

import vn.dh_shop.dto.payment.PaymentRequestDTO;
import vn.dh_shop.dto.payment.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO payWithVnpay (PaymentRequestDTO request);
}
