package vn.dh_shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.dto.request.ItemRequestDTO;
import vn.dh_shop.dto.response.ApiResponse;
import vn.dh_shop.dto.response.ItemResponseDTO;
import vn.dh_shop.dto.response.PageItemsResponseDTO;
import vn.dh_shop.service.ItemService;

@RestController
@RequestMapping("/api/items")
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
    public ResponseEntity<ApiResponse<PageItemsResponseDTO>> getAllItem(
            @RequestParam int pageNo, @RequestParam int pageSize) {
        PageItemsResponseDTO response = itemService.getAllItem(pageNo, pageSize);
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

    @GetMapping(value = "/categories/{categoryId}")
    public ResponseEntity<ApiResponse<PageItemsResponseDTO>> getItemByCategoryId
            (@PathVariable Long categoryId, @RequestParam int pageNo, @RequestParam int pageSize) {
        PageItemsResponseDTO response = itemService.getItemByCategoryId(categoryId, pageNo, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách thành công", response));
    }

    @GetMapping(value = "/categories/{categoryId}/brands/{brandId}")
    public ResponseEntity<ApiResponse<PageItemsResponseDTO>> getItemByBrandId
            (@PathVariable Long categoryId, @PathVariable Long brandId, @RequestParam int pageNo, @RequestParam int pageSize) {
        PageItemsResponseDTO response = itemService.getItemByBrandId(categoryId, brandId, pageNo, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách thành công", response));
    }
}
