package vn.dh_shop.dto.order;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.dh_shop.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Builder
@JsonPropertyOrder({"createdAt","orderId", "status", "totalPrice", "items"})
public class OrderResponseDTO {
    private Long orderId;
    private List<OrderItemResponseDTO> items;
    private Long totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
