package vn.dh_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import vn.dh_shop.entity.CartItem;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
    List<CartItem> findByCartId (Long cartId);
    @Modifying
    void deleteByCartId (Long cartId);
}
