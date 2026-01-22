package vn.dh_shop.dto.cart.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateCartItemRequestDTO {
    @NotNull(message = "quantity must be not null")
    @Min(value = 0, message = "quantity must be >= 0")
    private Integer quantity;
}
