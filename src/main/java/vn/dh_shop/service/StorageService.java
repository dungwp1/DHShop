package vn.dh_shop.service;

import vn.dh_shop.dto.storage.StorageRequestDTO;
import vn.dh_shop.dto.storage.StorageResponseDTO;

import java.util.List;

public interface StorageService {
    StorageResponseDTO addStorage(StorageRequestDTO request);

    List<StorageResponseDTO> getAllStorage();

    StorageResponseDTO updateStorage(Long ramId, StorageRequestDTO request);

    void deleteStorage(Long storageId);

}
