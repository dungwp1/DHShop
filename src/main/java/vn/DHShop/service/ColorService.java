package vn.DHShop.service;

import vn.DHShop.dto.request.ColorRequestDTO;
import vn.DHShop.dto.response.ColorResponseDTO;

import java.util.List;

public interface ColorService {
    ColorResponseDTO addColor(ColorRequestDTO request);

    List<ColorResponseDTO> getAllColor();

    void deleteColor(Long id);
}
