package vn.DHShop.service;

import org.springframework.data.domain.Page;
import vn.DHShop.dto.request.ModelRequestDTO;
import vn.DHShop.dto.response.ModelResponseDTO;
import vn.DHShop.entity.Model;

import java.util.List;

public interface ModelService {
    ModelResponseDTO addModel(Long categoryId, Long brandId, ModelRequestDTO request);

    List<ModelResponseDTO> getAllModels(Long categoryId, Long brandId);

    ModelResponseDTO getModelById(Long categoryId, Long brandId, Long modelId);

    void deleteModelById(Long categoryId, Long brandId, Long modelId);

    List<ModelResponseDTO> getModels(Long categoryId, Long brandId, int pageNo, int pageSize);
}
