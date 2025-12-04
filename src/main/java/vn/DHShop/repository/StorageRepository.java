package vn.DHShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.DHShop.entity.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    boolean existsByName(String name);
}
