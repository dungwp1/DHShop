package vn.dh_shop.service;

import vn.dh_shop.dto.request.ModelRequestDTO;
import vn.dh_shop.dto.response.ModelResponseDTO;

import java.util.List;

public interface ModelService {
    ModelResponseDTO addModel(Long categoryId, Long brandId, ModelRequestDTO request);

    List<ModelResponseDTO> getAllModels(Long categoryId, Long brandId);

    ModelResponseDTO getModelById(Long categoryId, Long brandId, Long modelId);

    void deleteModelById(Long categoryId, Long brandId, Long modelId);

    List<ModelResponseDTO> getModels(Long categoryId, Long brandId, int pageNo, int pageSize);
}
