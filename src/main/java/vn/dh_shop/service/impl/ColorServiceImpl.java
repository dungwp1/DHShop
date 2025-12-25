package vn.dh_shop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.ColorRequestDTO;
import vn.dh_shop.dto.response.ColorResponseDTO;
import vn.dh_shop.entity.Color;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.ColorRepository;
import vn.dh_shop.service.ColorService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    @Override
    public ColorResponseDTO addColor(ColorRequestDTO request) {
//        Check tồn tại color
        Color isColor = colorRepository.findByName(request.getName());
        if (isColor != null) throw new BadRequestException("Color đã tồn tại trong hệ thống");

//        Tạo mới entity color
        Color color = new Color();
        color.setName(request.getName());
        color.setHexColor(request.getHexColor());

//        Lưu vào db
        Color savedColor = colorRepository.save(color);

//        Map sang response

        ColorResponseDTO response = new ColorResponseDTO();
        response.setId(savedColor.getId());
        response.setName(savedColor.getName());
        response.setHexColor(savedColor.getHexColor());

        return response;
    }

    @Override
    public List<ColorResponseDTO> getAllColor() {
        List<Color> listColor = colorRepository.findAll();
//        Map sang response
        List<ColorResponseDTO> listResponse = new ArrayList<>();
        listColor.forEach(item -> {
            ColorResponseDTO response = new ColorResponseDTO();
            response.setId(item.getId());
            response.setName(item.getName());
            response.setHexColor(item.getHexColor());

            listResponse.add(response);
        });
        return listResponse;
    }

    @Override
    public void deleteColor(Long id) {
//        Check color
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy color có id: " + id));
//        Xóa color
        colorRepository.delete(color);
    }
}
