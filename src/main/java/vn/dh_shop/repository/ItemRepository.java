package vn.dh_shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dh_shop.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByCategoryId(Long categoryId, Pageable page);

    Page<Item> findAllByBrandId(Long brandId, Pageable pageable);

}
