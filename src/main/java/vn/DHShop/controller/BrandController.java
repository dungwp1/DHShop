package vn.DHShop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.DHShop.dto.request.BrandRequestDTO;
import vn.DHShop.dto.response.ApiResponse;
import vn.DHShop.dto.response.BrandResponseDTO;
import vn.DHShop.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/categories/{categoryId}/brands")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponseDTO>> addBrand(@Valid @RequestBody BrandRequestDTO request) {
        BrandResponseDTO response = brandService.addBrand(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Brand created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponseDTO>>> getAllBrands(@Valid @PathVariable Long categoryId) {
        List<BrandResponseDTO> listResponse = brandService.getAllBrands(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "All Brands got successfully", listResponse));
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<ApiResponse> deleteBrand(@Valid @PathVariable Long brandId){
        brandService.deleteBrand(brandId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Brand deleted successfully"));
    }
}
