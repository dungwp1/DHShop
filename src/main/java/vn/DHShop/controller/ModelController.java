package vn.DHShop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.DHShop.dto.request.ModelRequestDTO;
import vn.DHShop.dto.response.ApiResponse;
import vn.DHShop.dto.response.ModelResponseDTO;
import vn.DHShop.entity.Model;
import vn.DHShop.service.ModelService;

import java.util.List;

@RestController
@RequestMapping("/categories/{categoryId}/brands/{brandId}/models")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ModelController {
    private final ModelService modelService;

    @PostMapping
    public ResponseEntity<ApiResponse<ModelResponseDTO>> addModel(@PathVariable Long categoryId, @PathVariable Long brandId, @Valid @RequestBody ModelRequestDTO request) {
        ModelResponseDTO response = modelService.addModel(categoryId, brandId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm " + request.getName() + " vào database thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ModelResponseDTO>>> getAllModels(@PathVariable Long categoryId, @PathVariable Long brandId) {
        List<ModelResponseDTO> response = modelService.getAllModels(categoryId, brandId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách Model thành công", response));
    }

    @GetMapping(value = "/{modelId}")
    public ResponseEntity<ApiResponse<ModelResponseDTO>> getModelById(@PathVariable Long categoryId, @PathVariable Long brandId, @PathVariable Long modelId) {
        ModelResponseDTO response = modelService.getModelById(categoryId, brandId, modelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy " + response.getName() + " từ database thành công", response));
    }

    @DeleteMapping(value = "/{modelId}")
    public ResponseEntity<ApiResponse> deleteModelById(@PathVariable Long categoryId, @PathVariable Long brandId, @PathVariable Long modelId) {
        modelService.deleteModelById(categoryId, brandId, modelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Xóa Model thành công"));

    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<List<ModelResponseDTO>>> getAllModels(
            @PathVariable Long categoryId, @PathVariable Long brandId, @RequestParam int pageNo, @RequestParam int pageSize) {
        List<ModelResponseDTO> response = modelService.getModels(categoryId, brandId, pageNo, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách Model thành công", response));
    }
}
