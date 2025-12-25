package vn.dh_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dh_shop.entity.Ram;

@Repository
public interface RamRepository extends JpaRepository<Ram, Long> {
    Ram findByName(String name);
}
