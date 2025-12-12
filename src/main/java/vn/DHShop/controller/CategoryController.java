package vn.DHShop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.DHShop.dto.request.CategoryRequestDTO;
import vn.DHShop.dto.response.ApiResponse;
import vn.DHShop.dto.response.CategoryResponseDTO;
import vn.DHShop.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> addCategory(@Valid @RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO response = categoryService.addCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm " + request.getName() + " vào database thành công", response));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories() {
        List<CategoryResponseDTO> listResponse = categoryService.getAllCategories();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách Category thành công", listResponse));
    }

    @GetMapping(value = "/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(@PathVariable Long categoryId) {
        CategoryResponseDTO response = categoryService.getCategoryById(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy " + response.getName() + " từ database thành công", response));
    }

    @PutMapping(value = "/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategoryId(@PathVariable Long categoryId, @RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO response = categoryService.updateCategoryById(categoryId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật " + response.getName() + " thành công", response));
    }

    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Xóa thành công"));
    }

}
