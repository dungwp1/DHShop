package vn.dh_shop.service;

import vn.dh_shop.dto.request.BrandRequestDTO;
import vn.dh_shop.dto.response.BrandResponseDTO;

import java.util.List;

public interface BrandService {
    BrandResponseDTO addBrand(Long categoryId, BrandRequestDTO request);
    List<BrandResponseDTO> getAllBrands(Long categoryId);
    void deleteBrand(Long categoryId, Long brandId);

}
