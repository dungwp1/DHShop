package vn.dh_shop.service;

import vn.dh_shop.dto.request.CategoryRequestDTO;
import vn.dh_shop.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    
    CategoryResponseDTO addCategory(CategoryRequestDTO request);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO updateCategoryById(Long id, CategoryRequestDTO request);

    void deleteCategory(Long id);

}
