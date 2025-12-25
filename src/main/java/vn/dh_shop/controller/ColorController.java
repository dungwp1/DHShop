package vn.dh_shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.dto.request.ColorRequestDTO;
import vn.dh_shop.dto.response.ApiResponse;
import vn.dh_shop.dto.response.ColorResponseDTO;
import vn.dh_shop.service.ColorService;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ColorController {
    private final ColorService colorService;

    @PostMapping
    public ResponseEntity<ApiResponse<ColorResponseDTO>> addColor(@Valid @RequestBody ColorRequestDTO request) {
        ColorResponseDTO response = colorService.addColor(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), response.getName() + " đã được thêm vào db", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ColorResponseDTO>>> getAllColor() {
        List<ColorResponseDTO> listResponse = colorService.getAllColor();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách color thành công", listResponse));
    }

    @DeleteMapping(value = "/{colorId}")
    public ResponseEntity<ApiResponse> deleteColor(@PathVariable Long colorId) {
        colorService.deleteColor(colorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Xóa thành công"));
    }
}
