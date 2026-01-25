package vn.dh_shop.dto.cart.checkout;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonPropertyOrder({"canCheckout", "subTotal", "discount", "shippingFee", "finalPrice", "items"})
public class CheckoutResponseDTO {
    private List<CheckoutItemResponseDTO> items;
    private Long subtotal;
    private Long shippingFee;
    private Long discount;
    private Long finalPrice;
    private Boolean canCheckout;

    public static CheckoutResponseDTO notAllowed() {
        return CheckoutResponseDTO.builder()
                .items(List.of())
                .subtotal(0L)
                .shippingFee(0L)
                .discount(0L)
                .finalPrice(0L)
                .canCheckout(false)
                .build();
    }
}
