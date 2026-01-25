package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.dh_shop.dto.cart.cart.CartItemResponseDTO;
import vn.dh_shop.dto.cart.cart.CartResponseDTO;
import vn.dh_shop.entity.Cart;
import vn.dh_shop.entity.CartItem;
import vn.dh_shop.entity.Item;
import vn.dh_shop.entity.User;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.CartItemRepository;
import vn.dh_shop.repository.CartRepository;
import vn.dh_shop.repository.ItemRepository;
import vn.dh_shop.repository.UserRepository;
import vn.dh_shop.security.util.SecurityUtils;
import vn.dh_shop.service.CartService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final SecurityUtils securityUtils;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartResponseDTO addtoCart(Long itemId, Integer quantity) {
//        Xác định user
        Long userId = securityUtils.getUserId();
        if (userId == null) throw new BadRequestException("Không tìm thấy user");
//        Lấy cart của user
        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseGet(()->{
                    Cart newCart = new Cart();
                    User user = userRepository.findById(userId)
                            .orElseThrow(()-> new BadRequestException("Không tìm thấy user"));
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
//        Valdidate Item
        if (quantity < 1) throw new BadRequestException("Quantity phải lớn hơn 0");
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new BadRequestException("Không tìm thấy item"));

//        Kiểm tra item có tồn tại trong cart chưa
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId);
        if (cartItem == null) {
//            tạo cartItem
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setItem(item);
            newCartItem.setQuantity(quantity);
            cartItem = cartItemRepository.save(newCartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
            cartItem = cartItemRepository.save(cartItem);
        }

//        Get list cart item
        List<CartItem> listCartItem = cartItemRepository.findAllByCartId(cart.getId());

        return buildCartResponse(listCartItem);

    }

    @Override
    public CartResponseDTO getCurrentCart() {
//        get userid
        Long userId = securityUtils.getUserId();
        if(userId == null) throw new BadRequestException("Không tìm thấy user");
//        get cart by userid
        Optional<Cart> cartOpt = cartRepository.findCartByUserId(userId);
        if (cartOpt.isEmpty()) {
            return CartResponseDTO.empty();
        }
        Cart cart = cartOpt.get();
//      get list cartitem
        List<CartItem> listCartItem = cartItemRepository.findAllByCartId(cart.getId());
//        build cartitem response

        return buildCartResponse(listCartItem);
    }

    @Override
    public CartResponseDTO updateCurrentCart(Integer quantity, Long itemId) {
        if(quantity < 0 ) throw new BadRequestException("Quantity phải lớn hơn hoặc bằng 0");
//        Get userId
        Long userId = securityUtils.getUserId();
        if (userId == null) throw new BadRequestException("Không tìm thấy user");
//        Get card theo userId
        Optional<Cart> cartOpt = cartRepository.findCartByUserId(userId);
        if (cartOpt.isEmpty()) return CartResponseDTO.empty();

        //        Get cartItem theo cardId và itemId
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cartOpt.get().getId(), itemId);
        if(cartItem == null) throw new EntityNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng");
        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
//        Build response DTO
        List<CartItem> listCartItem = cartItemRepository.findAllByCartId(cartOpt.get().getId());
        return buildCartResponse(listCartItem);
    }

    @Override
    public CartResponseDTO deleteCurrentCartItem(Long itemId) {
//        Get userId
        Long userId = securityUtils.getUserId();
        if (userId == null) throw new BadRequestException("Không tìm thấy user");
//        Get card theo userid
        Optional<Cart> cart = cartRepository.findCartByUserId(userId);
        if (cart.isEmpty()) return CartResponseDTO.empty();
//        Get cartItem theo cardId và itemId
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.get().getId(), itemId);
        if (cartItem == null) throw new EntityNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng");
//        Xóa cartItem
        cartItemRepository.delete(cartItem);
        List<CartItem> listCartItem = cartItemRepository.findAllByCartId(cart.get().getId());
        return buildCartResponse(listCartItem);
    }

    @Override
    @Transactional
    public CartResponseDTO clearCart() {
        //        Get userId
        Long userId = securityUtils.getUserId();
        if (userId == null) throw new BadRequestException("Không tìm thấy user");
//        Get card theo userid
        Optional<Cart> cart = cartRepository.findCartByUserId(userId);
        if (cart.isEmpty()) return CartResponseDTO.empty();
//        Xóa cartItems
        cartItemRepository.deleteALlByCartId(cart.get().getId());
        List<CartItem> listCartItem = cartItemRepository.findAllByCartId(cart.get().getId());
        return buildCartResponse(listCartItem);
    }

    private CartResponseDTO buildCartResponse(List<CartItem> items) {
        List<CartItemResponseDTO> listCartItemResponse = new ArrayList<>();
        Long totalPrice = 0L;
        Integer totalQuantity = 0;
        for (CartItem i : items) {

            CartItemResponseDTO cartItemResponse = new CartItemResponseDTO();
            cartItemResponse.setItemId(i.getItem().getId());
            cartItemResponse.setName(i.getItem().getModel().getName());
            cartItemResponse.setPrice(i.getItem().getPrice());
            cartItemResponse.setQuantity(i.getQuantity());
            cartItemResponse.setSubtotalPrice(i.getItem().getPrice() * i.getQuantity());
            listCartItemResponse.add(cartItemResponse);

            totalPrice += cartItemResponse.getSubtotalPrice();
            totalQuantity += cartItemResponse.getQuantity();


        };

//        build cart response
        return CartResponseDTO.builder()
                .isEmpty(listCartItemResponse.isEmpty())
                .items(listCartItemResponse)
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .build();


    }
}
