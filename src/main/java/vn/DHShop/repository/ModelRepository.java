package vn.DHShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.DHShop.entity.Model;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findAllByBrandId(Long brandId);

    Page<Model> findModelsByBrandId(Long brandId, Pageable pageable);
}
