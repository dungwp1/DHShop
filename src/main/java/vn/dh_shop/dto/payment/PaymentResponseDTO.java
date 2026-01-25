package vn.dh_shop.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.dh_shop.entity.enums.OrderStatus;
import vn.dh_shop.entity.enums.PaymentMethod;
import vn.dh_shop.entity.enums.PaymentStatus;

@Builder
@Setter
@Getter
public class PaymentResponseDTO {
    private Long orderId;
    private PaymentMethod method;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private String message;
    private String redirectUrl;
}
