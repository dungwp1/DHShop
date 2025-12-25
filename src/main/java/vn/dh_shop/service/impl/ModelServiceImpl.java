package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.ModelRequestDTO;
import vn.dh_shop.dto.response.BrandResponseDTO;
import vn.dh_shop.dto.response.ModelResponseDTO;
import vn.dh_shop.entity.Brand;
import vn.dh_shop.entity.Model;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.BrandRepository;
import vn.dh_shop.repository.ModelRepository;
import vn.dh_shop.service.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelServiceImpl implements ModelService {
    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    @Override
    public ModelResponseDTO addModel(Long categoryId, Long brandId, ModelRequestDTO request) {
//        Check brandId entity
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand có id là: " + brandId));
        if (!Objects.equals(brand.getCategory().getId(), categoryId)) {
            throw new BadRequestException("Brand và Category không khớp");
        }
//        Map sang brandDTO
        BrandResponseDTO brandResponse = new BrandResponseDTO(brand.getId(), brand.getName());

        //        Tạo entity model
        Model model = new Model();
        model.setName(request.getName());
        model.setBrand(brand);
//        Lưu vào db
        Model savedModel = modelRepository.save(model);
//        Tạo responseDTO
        ModelResponseDTO response = new ModelResponseDTO();
        response.setId(savedModel.getId());
        response.setName(savedModel.getName());
        response.setBrand(brandResponse);

        return response;
    }

    @Override
    public List<ModelResponseDTO> getAllModels(Long categoryId, Long brandId) {
//        Check brandId
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand có id là: " + brandId));
        if (!Objects.equals(brand.getCategory().getId(), categoryId)) {
            throw new BadRequestException("Brand và Category không khớp");
        }
//        Get ra list entity model
        List<Model> listModel = modelRepository.findAllByBrandId(brandId);
//        Map sang DTO
        List<ModelResponseDTO> listResponse = new ArrayList<>();
        listModel.forEach(item -> {
            ModelResponseDTO response = new ModelResponseDTO();
            response.setId(item.getId());
            response.setName(item.getName());
            response.setBrand(new BrandResponseDTO(item.getBrand().getId(), item.getBrand().getName()));
            listResponse.add(response);
        });
        return listResponse;
    }

    @Override
    public ModelResponseDTO getModelById(Long categoryId, Long brandId, Long modelId) {
//        Check brandId
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand có id là: " + brandId));
        if (!Objects.equals(brand.getCategory().getId(), categoryId)) {
            throw new BadRequestException("Brand và Category không khớp");
        }
//        Map sang DTO
        BrandResponseDTO brandResponse = new BrandResponseDTO(brand.getId(), brand.getName());


//        Get entity
        Model model = modelRepository.findById(modelId)
                .orElseThrow(() -> new EntityNotFoundException("Model not found!!!"));
//        Map sang responseDTO
        ModelResponseDTO response = new ModelResponseDTO();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setBrand(brandResponse);
        return response;
    }

    @Override
    public void deleteModelById(Long categoryId, Long brandId, Long modelId) {
//        Check brand
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand có id là: " + brandId));
        if (!Objects.equals(brand.getCategory().getId(), categoryId)) {
            throw new BadRequestException("Brand và Category không khớp");
        }
//        Check model
        Model model = modelRepository.findById(modelId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Model có id là: " + modelId));
        if (!Objects.equals(model.getBrand().getId(), brandId))
            throw new BadRequestException("Model và Brand không khớp");
        modelRepository.delete(model);
        log.info("Deleted model {} of brand {}", modelId, brandId);
    }

    @Override
    public List<ModelResponseDTO> getModels(Long categoryId, Long brandId, int pageNo, int pageSize) {
//        Validate pageNo
        if (pageNo == 0) throw new BadRequestException("PageNo phải lớn hơn 0");
//        Check brandId
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Brand có id là: " + brandId));
        if (!Objects.equals(brand.getCategory().getId(), categoryId)) {
            throw new BadRequestException("Brand và Category không khớp");
        }
//        Map sang DTO
        BrandResponseDTO brandResponse = new BrandResponseDTO(brand.getId(), brand.getName());
//        Tạo đối tượng Pageable
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//        Lấy danh sách entity model
        Page<Model> models = modelRepository.findModelsByBrandId(brandId, pageable);
        log.info("getContent(): {}", models.getContent());
        log.info("getTotalPages(): {}", models.getTotalPages());
        log.info("getTotalElements(): {}", models.getTotalElements());
        log.info("getNumber(): {}", models.getNumber());
        log.info("getSize(): {}", models.getSize());
        log.info("hasNext(): {}", models.hasNext());
        log.info("hasPrevious(): {}", models.hasPrevious());
//        Map sang response
        List<ModelResponseDTO> listResponse = new ArrayList<>();
        models.forEach(item -> {
            ModelResponseDTO response = new ModelResponseDTO();
            response.setId(item.getId());
            response.setName(item.getName());
            response.setBrand(new BrandResponseDTO(item.getBrand().getId(), item.getBrand().getName()));
            listResponse.add(response);
        });
        return listResponse;
    }
}
