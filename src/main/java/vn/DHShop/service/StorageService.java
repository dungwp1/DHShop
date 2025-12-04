package vn.DHShop.service;

import vn.DHShop.dto.request.StorageRequestDTO;
import vn.DHShop.dto.response.StorageResponseDTO;

import java.util.List;

public interface StorageService {
    StorageResponseDTO addStorage(StorageRequestDTO request);

    List<StorageResponseDTO> getAllStorage();

    StorageResponseDTO updateStorage(Long ramId, StorageRequestDTO request);

    void deleteStorage(Long storageId);

}
