package vn.dh_shop.service;

import vn.dh_shop.dto.cart.cart.CartResponseDTO;

public interface CartService {
    CartResponseDTO addtoCart(Long itemId, Integer quantity);
    CartResponseDTO getCurrentCart();
    CartResponseDTO updateCurrentCart(Integer quantity, Long itemId);
    CartResponseDTO deleteCurrentCartItem(Long itemId);
    CartResponseDTO clearCart();




}
