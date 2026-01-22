package vn.dh_shop.dto.cart.checkout;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckoutItemResponseDTO {
    private Long itemId;
    private String name;
    private Long price;
    private Integer quantity;
    private Long subtotal;
}
