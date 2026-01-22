package vn.dh_shop.dto.cart.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class AddCartItemRequestDTO implements Serializable {
    @NotNull(message = "itemId must be not null")
    private Long itemId;
    @NotNull(message = "quantity must be not null")
    @Min(value = 1, message = "quantity must be greater than 0")
    private Integer quantity;
}
