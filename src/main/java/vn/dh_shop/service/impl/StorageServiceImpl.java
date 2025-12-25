package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.StorageRequestDTO;
import vn.dh_shop.dto.response.StorageResponseDTO;
import vn.dh_shop.entity.Storage;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.StorageRepository;
import vn.dh_shop.service.StorageService;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;

    @Override
    public StorageResponseDTO addStorage(StorageRequestDTO request) {
//        Check storage
        if (storageRepository.existsByName(request.getName()))
            throw new BadRequestException("Storage " + request.getName() + " đã tồn tại trong db");
//        Tạo entity cb lưu vào db
        Storage storage = new Storage();
        storage.setName(request.getName());
//        Lưu vào db
        Storage savedStorage = storageRepository.save(storage);
//        Map sang response
        return new StorageResponseDTO(savedStorage.getId(), savedStorage.getName());
    }

    @Override
    public List<StorageResponseDTO> getAllStorage() {
        List<Storage> listStorage = storageRepository.findAll();
        List<StorageResponseDTO> listResponse = new ArrayList<>();
        listStorage.forEach(item -> {
            StorageResponseDTO response = new StorageResponseDTO(item.getId(), item.getName());
            listResponse.add(response);
        });
        return listResponse;
    }

    @Override
    public StorageResponseDTO updateStorage(Long storageId, StorageRequestDTO request) {
        //        Get Storage
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Storage có id: " + storageId));
//        Update Storage
        storage.setName(request.getName());
//        Lưu db
        Storage savedStorage = storageRepository.save(storage);
//        Map response trả client
        return new StorageResponseDTO(savedStorage.getId(), savedStorage.getName());
    }

    @Override
    public void deleteStorage(Long storageId) {
//        Check Storage
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Storage có id: " + storageId));
//        Delete Storage
        storageRepository.delete(storage);
    }
}
