package vn.dh_shop.service;

import vn.dh_shop.dto.request.RamRequestDTO;
import vn.dh_shop.dto.response.RamResponseDTO;

import java.util.List;

public interface RamService {
    RamResponseDTO addRam(RamRequestDTO request);

    List<RamResponseDTO> getAllRam();

    RamResponseDTO updateRam(Long ramId, RamRequestDTO request);

    void deleteRam(Long id);

}
