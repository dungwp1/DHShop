package vn.DHShop.service;

import vn.DHShop.dto.request.CategoryRequestDTO;
import vn.DHShop.dto.response.CategoryResponseDTO;
import vn.DHShop.entity.Category;

import java.util.List;

public interface CategoryService {
    
    CategoryResponseDTO addCategory(CategoryRequestDTO request);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO updateCategoryById(Long id, CategoryRequestDTO request);

    void deleteCategory(Long id);

}
