package vn.dh_shop.dto.cart.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDTO {
    private Long itemId;
    private String name;
    private Long price;
    private Integer quantity;
    private Long subtotalPrice;
}
