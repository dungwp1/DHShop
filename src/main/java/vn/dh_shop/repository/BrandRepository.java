package vn.dh_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.dh_shop.entity.Brand;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllByCategoryId(Long categoryId);
}
