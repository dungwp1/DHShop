package vn.DHShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.DHShop.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findByName(String name);
}
