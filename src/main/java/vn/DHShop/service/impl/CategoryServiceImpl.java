package vn.DHShop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.GeneratedValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.DHShop.dto.request.CategoryRequestDTO;
import vn.DHShop.dto.response.CategoryResponseDTO;
import vn.DHShop.entity.Category;
import vn.DHShop.repository.CategoryRepository;
import vn.DHShop.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO addCategory(CategoryRequestDTO request) {
        Category category = new Category();
        category.setName(request.getName());


        Category savedCategory = categoryRepository.save(category);
        log.info("Category add success with id: " + savedCategory.getId());

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setName(savedCategory.getName());
        response.setId(savedCategory.getId());
        return response;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> allCategory = categoryRepository.findAll();
        log.info("Get All Category success: " + allCategory);

        List<CategoryResponseDTO> listResponse = new ArrayList<>();
        allCategory.forEach(savedCategory -> {
            CategoryResponseDTO response = new CategoryResponseDTO();
            response.setName(savedCategory.getName());
            response.setId(savedCategory.getId());

            listResponse.add(response);
        });
        return listResponse;
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }


    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
