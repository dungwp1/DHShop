package vn.DHShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.DHShop.entity.Brand;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllByCategoryId(Long categoryId);
}
