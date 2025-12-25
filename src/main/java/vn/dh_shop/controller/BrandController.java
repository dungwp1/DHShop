package vn.dh_shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.dto.request.BrandRequestDTO;
import vn.dh_shop.dto.response.ApiResponse;
import vn.dh_shop.dto.response.BrandResponseDTO;
import vn.dh_shop.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/categories/{categoryId}/brands")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponseDTO>> addBrand(@PathVariable Long categoryId, @Valid @RequestBody BrandRequestDTO request) {
        BrandResponseDTO response = brandService.addBrand(categoryId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm " + request.getName() + " vào database thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponseDTO>>> getAllBrands(@PathVariable Long categoryId) {
        List<BrandResponseDTO> listResponse = brandService.getAllBrands(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách Brand thành công", listResponse));
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<ApiResponse> deleteBrand(@PathVariable Long categoryId, @PathVariable Long brandId) {
        brandService.deleteBrand(categoryId, brandId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Xóa Brand thành công"));
    }
}
