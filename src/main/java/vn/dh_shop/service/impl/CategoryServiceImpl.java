package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.CategoryRequestDTO;
import vn.dh_shop.dto.response.CategoryResponseDTO;
import vn.dh_shop.entity.Category;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.CategoryRepository;
import vn.dh_shop.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO addCategory(CategoryRequestDTO request) {
//        Check tồn tại category
        Category isCategory = categoryRepository.findByName(request.getName());
        if (isCategory != null) throw new BadRequestException("Category đã tồn tại trong hệ thống");

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
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category có id là: " + id));

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }

    @Override
    public CategoryResponseDTO updateCategoryById(Long id, CategoryRequestDTO request) {
//        Check category
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category có id là: " + id));
//        Update category theo request
        category.setName(request.getName());
//        Lưu vào DB
        Category savedCategory = categoryRepository.save(category);

        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(savedCategory.getId());
        response.setName(savedCategory.getName());

        return response;
    }


    @Override
    public void deleteCategory(Long id) {
        //        Check category
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category có id là: " + id));
        categoryRepository.delete(category);
    }


}
