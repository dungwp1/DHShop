package vn.dh_shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.cart.checkout.CheckoutItemResponseDTO;
import vn.dh_shop.dto.cart.checkout.CheckoutResponseDTO;
import vn.dh_shop.entity.Cart;
import vn.dh_shop.entity.CartItem;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.CartItemRepository;
import vn.dh_shop.repository.CartRepository;
import vn.dh_shop.security.util.SecurityUtils;
import vn.dh_shop.service.CartService;
import vn.dh_shop.service.CheckoutService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final SecurityUtils securityUtils;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public CheckoutResponseDTO checkoutPreview() {
//        Get userId
        Long userId = securityUtils.getUserId();
        if (userId == null) throw new BadRequestException("Không tìm thấy user");
//        Get cart theo userid
        Optional<Cart> cart = cartRepository.findCartByUserId(userId);
        if (cart.isEmpty()) throw new BadRequestException("Không tìm thấy giỏ hàng");
//        Get cartItem theo cartId
        List<CartItem> cartItemList = cartItemRepository.findByCartId(cart.get().getId());
        if (cartItemList.isEmpty()) throw new BadRequestException("Giỏ hàng trống");
//        Build checkoutItem
        List<CheckoutItemResponseDTO> listCheckoutItem = new ArrayList<>();
        Long subtotal = 0L;
        Long shippingFee = 0L;
        Long discount = 0L;
        for (CartItem cartItem : cartItemList) {
            CheckoutItemResponseDTO checkoutItem =
                    CheckoutItemResponseDTO
                            .builder()
                            .itemId(cartItem.getItem().getId())
                            .name(cartItem.getItem().getModel().getName())
                            .price(cartItem.getItem().getPrice())
                            .quantity(cartItem.getQuantity())
                            .subtotal(cartItem.getItem().getPrice()*cartItem.getQuantity())
                            .build();
            listCheckoutItem.add(checkoutItem);
            subtotal += checkoutItem.getSubtotal();
        }

        Long finalPrice = subtotal + shippingFee - discount;

//        Build responseDTO


        return CheckoutResponseDTO.builder()
                .items(listCheckoutItem)
                .subTotal(subtotal)
                .shippingFee(shippingFee)
                .discount(discount)
                .finalPrice(finalPrice)
                .canCheckout(true)
                .build();
    }
}
