package vn.DHShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.DHShop.entity.Ram;

@Repository
public interface RamRepository extends JpaRepository<Ram, Long> {
    Ram findByName(String name);
}
