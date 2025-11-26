package vn.DHShop.service;

import vn.DHShop.dto.request.CategoryRequestDTO;
import vn.DHShop.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO addCategory(CategoryRequestDTO request);
    List<CategoryResponseDTO> getAllCategory();
    void deleteCategory(Long id);
}
