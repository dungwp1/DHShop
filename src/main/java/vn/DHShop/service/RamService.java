package vn.DHShop.service;

import vn.DHShop.dto.request.RamRequestDTO;
import vn.DHShop.dto.response.RamResponseDTO;

import java.util.List;

public interface RamService {
    RamResponseDTO addRam(RamRequestDTO request);

    List<RamResponseDTO> getAllRam();

    RamResponseDTO updateRam(Long ramId, RamRequestDTO request);

    void deleteRam(Long id);

}
