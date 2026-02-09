package vn.dh_shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.dto.common.ApiResponse;
import vn.dh_shop.dto.order.OrderResponseDTO;
import vn.dh_shop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder () {
        OrderResponseDTO response = orderService.createOrder();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "CREATE_ORDER_SUCCESS", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrder() {
        List<OrderResponseDTO> response = orderService.getOrder();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "GET_ORDER_SUCCESS", response));
    }
    @GetMapping(value = "/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO response = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "GET_ORDER_SUCCESS", response));
    }


}
