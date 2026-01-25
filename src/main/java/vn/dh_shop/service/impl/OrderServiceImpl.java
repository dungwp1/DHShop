package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.dh_shop.dto.order.OrderItemResponseDTO;
import vn.dh_shop.dto.order.OrderResponseDTO;
import vn.dh_shop.entity.*;
import vn.dh_shop.entity.enums.OrderStatus;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.*;
import vn.dh_shop.security.util.SecurityUtils;
import vn.dh_shop.service.OrderService;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder() {
// lấy userId
        Long userId = securityUtils.getUserId();
        if (userId == null) throw new BadRequestException("Không tìm thấy user");
//        lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy user"));
//        lấy cart
        Optional<Cart> cart = cartRepository.findCartByUserId(userId);
        if(cart.isEmpty()) throw new BadRequestException("Không tìm thấy giỏ hàng");
//        lấy cartitem
        List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cart.get().getId());
        if(cartItemList.isEmpty()) throw new BadRequestException("Giỏ hàng trống");
//        tạo entity order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(0L);

        order = orderRepository.save(order);
//        tạo entity orderitem
        Long totalPrice = 0L;
        for (CartItem cartItem : cartItemList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
                    orderItem.setItemId(cartItem.getItem().getId());
                    orderItem.setName(cartItem.getItem().getModel().getName());
                    orderItem.setPrice(cartItem.getItem().getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setSubtotal(cartItem.getItem().getPrice() * cartItem.getQuantity());
            orderItem = orderItemRepository.save(orderItem);
            totalPrice += orderItem.getSubtotal();
        }

        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);

//        clear cart
        cartItemRepository.deleteAll(cartItemList);
//        tạo responseItemDTO
        List<OrderItemResponseDTO> orderItemResponseList = new ArrayList<>();
        List<OrderItem> orderItemList = orderItemRepository.findAllByOrderId(order.getId());
        totalPrice = 0L;
        for (OrderItem orderItem : orderItemList) {
            OrderItemResponseDTO orderItemResponse = OrderItemResponseDTO.builder()
                    .name(orderItem.getName())
                    .price(orderItem.getPrice())
                    .quantity(orderItem.getQuantity())
                    .subtotal(orderItem.getSubtotal())
                    .build();
            orderItemResponseList.add(orderItemResponse);

            totalPrice += orderItemResponse.getSubtotal();
        }

//        tạo responseDTO
        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .items(orderItemResponseList)
                .totalPrice(totalPrice)
                .status(order.getStatus())
                .createdAt(order.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();

    }

    @Override
    public List<OrderResponseDTO> getOrder() {
        Long userId = securityUtils.getUserId();
        if (userId == null) throw new BadRequestException("Không tìm thấy user");

//get order
        List<Order> orderList = orderRepository.findAllByUserId(userId);
        List<OrderResponseDTO> responseList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemRepository.findAllByOrderId(order.getId());

            List<OrderItemResponseDTO> orderItemResponseDTOList = new ArrayList<>();
            for (OrderItem orderItem : orderItemList) {
                OrderItemResponseDTO orderItemResponse = OrderItemResponseDTO.builder()
                        .name(orderItem.getName())
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity())
                        .subtotal(orderItem.getSubtotal())
                        .build();
                orderItemResponseDTOList.add(orderItemResponse);
            }
            OrderResponseDTO response =  OrderResponseDTO.builder()
                    .orderId(order.getId())
                    .items(orderItemResponseDTOList)
                    .totalPrice(order.getTotalPrice())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();
            responseList.add(response);
        }

        return responseList;
    }


}
