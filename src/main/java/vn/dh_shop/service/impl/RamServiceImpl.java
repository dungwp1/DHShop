package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.RamRequestDTO;
import vn.dh_shop.dto.response.RamResponseDTO;
import vn.dh_shop.entity.Ram;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.RamRepository;
import vn.dh_shop.service.RamService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RamServiceImpl implements RamService {
    private final RamRepository ramRepository;

    @Override
    public RamResponseDTO addRam(RamRequestDTO request) {
//        Check tồn tại ram
        Ram ramCheck = ramRepository.findByName(request.getName());
        if (ramCheck != null) throw new BadRequestException("Ram đã tồn tại trong db");
//        Tạo entity
        Ram ram = new Ram();
        ram.setName(request.getName());
//        Lưu vào db
        Ram savedRam = ramRepository.save(ram);
//        Map sang response

        return new RamResponseDTO(savedRam.getId(), savedRam.getName());
    }

    @Override
    public List<RamResponseDTO> getAllRam() {
        List<Ram> listRam = ramRepository.findAll();
        List<RamResponseDTO> listResponse = new ArrayList<>();
        listRam.forEach(item -> {
            RamResponseDTO response = new RamResponseDTO(item.getId(), item.getName());
            listResponse.add(response);
        });
        return listResponse;
    }

    @Override
    public RamResponseDTO updateRam(Long ramId, RamRequestDTO request) {
//        Get Ram
        Ram ram = ramRepository.findById(ramId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Ram có id: " + ramId));
//        Update Ram
        ram.setName(request.getName());
//        Lưu db
        Ram savedRam = ramRepository.save(ram);
//        Map response trả client
        return new RamResponseDTO(savedRam.getId(), savedRam.getName());
    }

    @Override
    public void deleteRam(Long id) {
//        Check Ram
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Ram có id: " + id));
//        Delete Ram
        ramRepository.delete(ram);
    }
}
