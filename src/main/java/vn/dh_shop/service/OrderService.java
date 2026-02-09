package vn.dh_shop.service;

import vn.dh_shop.dto.order.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder();
    List<OrderResponseDTO> getOrder();
    OrderResponseDTO getOrderById(Long orderId);

}
