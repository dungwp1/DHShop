package vn.DHShop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.DHShop.dto.request.StorageRequestDTO;
import vn.DHShop.dto.response.ApiResponse;
import vn.DHShop.dto.response.StorageResponseDTO;
import vn.DHShop.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("/storages")
@Validated
@RequiredArgsConstructor
@Slf4j
public class StorageController {
    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<ApiResponse<StorageResponseDTO>> addStorage(@Valid @RequestBody StorageRequestDTO request) {
        StorageResponseDTO response = storageService.addStorage(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), response.getName() + " đã thêm vào db", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StorageResponseDTO>>> getAllStorage() {
        List<StorageResponseDTO> listResponse = storageService.getAllStorage();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách Storage thành công", listResponse));
    }

    @PutMapping(value = "/{storageId}")
    public ResponseEntity<ApiResponse<StorageResponseDTO>> updateStorage(@PathVariable Long storageId, @Valid @RequestBody StorageRequestDTO request) {
        StorageResponseDTO response = storageService.updateStorage(storageId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật storage thành công", response));
    }

    @DeleteMapping(value = "/{storageId}")
    public ResponseEntity<ApiResponse> deleteStorage(@PathVariable Long storageId) {
        storageService.deleteStorage(storageId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Xóa thành công"));
    }
}
