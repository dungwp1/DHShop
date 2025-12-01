package vn.DHShop.service;

import vn.DHShop.dto.request.ModelRequestDTO;
import vn.DHShop.dto.response.ModelResponseDTO;

import java.util.List;

public interface ModelService {
    ModelResponseDTO addModel (Long brandId, ModelRequestDTO request);
    List<ModelResponseDTO> getAllModels (Long brandId);
    ModelResponseDTO getModelById (Long brandId, Long modelId);
    void deleteModelById (Long modelId);
}
