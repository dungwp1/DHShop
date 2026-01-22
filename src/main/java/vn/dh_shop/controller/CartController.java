package vn.dh_shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.dto.cart.cart.AddCartItemRequestDTO;
import vn.dh_shop.dto.cart.cart.CartResponseDTO;
import vn.dh_shop.dto.cart.cart.UpdateCartItemRequestDTO;
import vn.dh_shop.dto.common.ApiResponse;
import vn.dh_shop.service.CartService;

@RestController
@RequestMapping("/api/cart")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addtoCart(@Valid @RequestBody AddCartItemRequestDTO request) {
        log.info("Add item {} with quantity {} to cart", request.getItemId(), request.getQuantity());
        CartResponseDTO response = cartService.addtoCart(request.getItemId(), request.getQuantity());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "ADD_TO_CART_SUCCESS", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCurrentCart () {
        log.info("Get current cart");
        CartResponseDTO response = cartService.getCurrentCart();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "GET_CART_SUCCESS", response));
    }

    @PutMapping(value = "/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateCurrentCart(@Valid @RequestBody UpdateCartItemRequestDTO request, @PathVariable Long itemId) {
        log.info("Update cart item {}, quantity={}", itemId, request.getQuantity());
        CartResponseDTO response = cartService.updateCurrentCart(request.getQuantity(), itemId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "UPDATE_CART_SUCCESS", response));
    }

    @DeleteMapping(value = "/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CartResponseDTO>> deleteCurrentCartItem(@PathVariable Long itemId) {
        log.info("Delete cart item {}", itemId);
        CartResponseDTO response = cartService.deleteCurrentCartItem(itemId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "DELETE_CART_ITEM_SUCCESS", response));
    }
    @DeleteMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<CartResponseDTO>> clearCart() {
        log.info("Delete cart");
        CartResponseDTO response = cartService.clearCart();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "DELETE_CART_SUCCESS", response));
    }
}
