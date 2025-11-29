package vn.DHShop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.DHShop.dto.request.BrandRequestDTO;
import vn.DHShop.dto.response.BrandResponseDTO;
import vn.DHShop.dto.response.CategoryResponseDTO;
import vn.DHShop.entity.Brand;
import vn.DHShop.entity.Category;
import vn.DHShop.repository.BrandRepository;
import vn.DHShop.repository.CategoryRepository;
import vn.DHShop.service.BrandService;
import vn.DHShop.service.CategoryService;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @Override
    public BrandResponseDTO addBrand(BrandRequestDTO request) {
//      Check xem có chứa Category chưa
        Long id = request.getCategoryId();

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));

//      Tạo entity để lưu vào DB
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setCategory(category);

        Brand savedBrand = brandRepository.save(brand);
//      Tạo responseDTO để trả về controller
        BrandResponseDTO response = new BrandResponseDTO();
        response.setId(savedBrand.getId());
        response.setName(savedBrand.getName());

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        response.setCategory(categoryDTO);
        log.info("Creating brand {} for category {}", request.getName(), category.getName());
        return response;
    }

    @Override
    public List<BrandResponseDTO> getAllBrands(Long categoryId) {
        //      Check xem có chứa Category chưa
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new EntityNotFoundException("CategoryId of Brand not found"));
        //Map entity Category sang DTO Category
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
//        FindAll theo categoryId
        List<Brand> listBrand = brandRepository.findAllByCategoryId(categoryId);
//        Map sang responseDTO
        List<BrandResponseDTO> listResponse = new ArrayList<>();
        listBrand.forEach(item -> {
            BrandResponseDTO response = new BrandResponseDTO();
            response.setId(item.getId());
            response.setName(item.getName());
            response.setCategory(categoryDTO);

            listResponse.add(response);
        });
        log.info("Found {} brands for category {}", listBrand.size(), category.getName());
        return listResponse;
    }

    @Override
    public void deleteBrand(Long brandId) {
        brandRepository.deleteById(brandId);
    }
}
