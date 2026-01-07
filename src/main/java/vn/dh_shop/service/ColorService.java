package vn.dh_shop.service;

import vn.dh_shop.dto.color.ColorRequestDTO;
import vn.dh_shop.dto.color.ColorResponseDTO;

import java.util.List;

public interface ColorService {
    ColorResponseDTO addColor(ColorRequestDTO request);

    List<ColorResponseDTO> getAllColor();

    void deleteColor(Long id);
}
