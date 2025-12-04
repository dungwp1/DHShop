package vn.DHShop.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.DHShop.dto.request.ColorRequestDTO;
import vn.DHShop.dto.response.ColorResponseDTO;
import vn.DHShop.entity.Color;
import vn.DHShop.exception.BadRequestException;
import vn.DHShop.repository.ColorRepository;
import vn.DHShop.service.ColorService;

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
