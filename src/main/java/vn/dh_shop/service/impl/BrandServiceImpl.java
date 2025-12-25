package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.BrandRequestDTO;
import vn.dh_shop.dto.response.BrandResponseDTO;
import vn.dh_shop.dto.response.CategoryResponseDTO;
import vn.dh_shop.entity.Brand;
import vn.dh_shop.entity.Category;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.BrandRepository;
import vn.dh_shop.repository.CategoryRepository;
import vn.dh_shop.service.BrandService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public BrandResponseDTO addBrand(Long categoryId, BrandRequestDTO request) {
//      Check xem có chứa Category chưa
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category có id là: " + categoryId));

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
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Category có id là: " + categoryId));
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
    public void deleteBrand(Long categoryId, Long brandId) {
//        Check brand
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand có id là: " + brandId));
        if (!Objects.equals(brand.getCategory().getId(), categoryId))
            throw new BadRequestException("Brand và Category không khớp");
        brandRepository.delete(brand);
    }
}
