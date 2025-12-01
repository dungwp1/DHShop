package vn.DHShop.service;

import vn.DHShop.dto.request.BrandRequestDTO;
import vn.DHShop.dto.response.BrandResponseDTO;

import java.util.List;

public interface BrandService {
    BrandResponseDTO addBrand(Long categoryId, BrandRequestDTO request);
    List<BrandResponseDTO> getAllBrands(Long categoryId);
    void deleteBrand(Long categoryId, Long brandId);

}
