package vn.DHShop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.DHShop.dto.request.ModelRequestDTO;
import vn.DHShop.dto.response.ApiResponse;
import vn.DHShop.dto.response.ModelResponseDTO;
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
    public ResponseEntity<ApiResponse<ModelResponseDTO>> addModel (@PathVariable Long brandId, @Valid @RequestBody ModelRequestDTO request) {
        ModelResponseDTO response = modelService.addModel(brandId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Model Added Successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ModelResponseDTO>>> getAllModels (@PathVariable Long brandId) {
        List<ModelResponseDTO> response = modelService.getAllModels(brandId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Model got Successfully", response));
    }

    @GetMapping(value = "/{modelId}")
    public ResponseEntity<ApiResponse<ModelResponseDTO>> getModelById (@PathVariable Long brandId, @PathVariable Long modelId) {
        ModelResponseDTO response = modelService.getModelById(brandId, modelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Model by Id got successfully", response));
    }

    @DeleteMapping(value = "/{modelId}")
    public ResponseEntity<ApiResponse> deleteModelById (@PathVariable Long modelId) {
        modelService.deleteModelById(modelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Model Deleted successfully"));

    }
}
