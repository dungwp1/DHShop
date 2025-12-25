package vn.dh_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dh_shop.entity.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    boolean existsByName(String name);
}
