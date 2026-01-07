package vn.dh_shop.service;

import vn.dh_shop.dto.category.CategoryRequestDTO;
import vn.dh_shop.dto.category.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    
    CategoryResponseDTO addCategory(CategoryRequestDTO request);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO updateCategoryById(Long id, CategoryRequestDTO request);

    void deleteCategory(Long id);

}
