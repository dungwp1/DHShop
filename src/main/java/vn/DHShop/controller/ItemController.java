package vn.DHShop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.DHShop.dto.request.ItemRequestDTO;
import vn.DHShop.dto.response.ApiResponse;
import vn.DHShop.dto.response.ItemResponseDTO;
import vn.DHShop.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponseDTO>> addItem(@ModelAttribute ItemRequestDTO request) {
        log.info("Check controller ------- {}", request.getCategoryId());
        ItemResponseDTO response = itemService.addItem(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Thêm item thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponseDTO>>> getAllItem() {
        List<ItemResponseDTO> response = itemService.getAllItem();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách item thành công", response));
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<ApiResponse<ItemResponseDTO>> getItemById(@PathVariable Long itemId) {
        ItemResponseDTO response = itemService.getItemById(itemId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy item thành công", response));
    }

    @DeleteMapping(value = "/{itemId}")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Xóa thành công"));
    }
}
