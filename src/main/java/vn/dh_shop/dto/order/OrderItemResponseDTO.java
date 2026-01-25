package vn.dh_shop.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemResponseDTO {
    private String name;
    private Long price;
    private Integer quantity;
    private Long subtotal;
}
