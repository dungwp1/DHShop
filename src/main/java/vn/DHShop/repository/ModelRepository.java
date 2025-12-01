package vn.DHShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.DHShop.entity.Model;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findAllByBrandId(Long brandId);
}
