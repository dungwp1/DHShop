package vn.dh_shop.dto.cart.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Builder
public class CartResponseDTO {
    private List<CartItemResponseDTO> items;
    private Integer totalQuantity;
    private Long totalPrice;
    private Boolean isEmpty;

    public static CartResponseDTO empty() {
        return CartResponseDTO.builder()
                .items(List.of())
                .totalQuantity(0)
                .totalPrice(0L)
                .isEmpty(true)
                .build();
    }

}
