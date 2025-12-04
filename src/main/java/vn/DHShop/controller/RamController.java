package vn.DHShop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.DHShop.dto.request.RamRequestDTO;
import vn.DHShop.dto.response.ApiResponse;
import vn.DHShop.dto.response.RamResponseDTO;
import vn.DHShop.service.RamService;

import java.util.List;

@RestController
@RequestMapping("/rams")
@Slf4j
@Validated
@RequiredArgsConstructor
public class RamController {
    private final RamService ramService;

    @PostMapping
    public ResponseEntity<ApiResponse<RamResponseDTO>> addRam(@Valid @RequestBody RamRequestDTO request) {
        RamResponseDTO response = ramService.addRam(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), response.getName() + " đã thêm vào db", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RamResponseDTO>>> getAllRam() {
        List<RamResponseDTO> listResponse = ramService.getAllRam();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách Ram thành công", listResponse));
    }

    @PutMapping(value = "/{ramId}")
    public ResponseEntity<ApiResponse<RamResponseDTO>> updateRam(@PathVariable Long ramId, @Valid @RequestBody RamRequestDTO request) {
        RamResponseDTO response = ramService.updateRam(ramId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật Ram thành công thành: " + response.getName(), response));
    }

    @DeleteMapping(value = "/{ramId}")
    public ResponseEntity<ApiResponse> deleteRam(@PathVariable Long ramId) {
        ramService.deleteRam(ramId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Xóa thành công"));
    }
}
