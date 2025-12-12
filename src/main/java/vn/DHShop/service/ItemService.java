package vn.DHShop.service;

import vn.DHShop.dto.request.ItemRequestDTO;
import vn.DHShop.dto.response.ItemResponseDTO;
import vn.DHShop.dto.response.PageItemsResponseDTO;

import java.util.List;

public interface ItemService {
    ItemResponseDTO addItem(ItemRequestDTO request);

    PageItemsResponseDTO getAllItem(int pageNo, int pageSize);

    ItemResponseDTO getItemById(Long itemId);

    PageItemsResponseDTO getItemByCategoryId(Long categoryId, int pageNo, int pageSize);

    PageItemsResponseDTO getItemByBrandId(Long categoryId, Long brandId, int pageNo, int pageSize);

    PageItemsResponseDTO getItemByModelId(Long categoryId);

    void deleteItem(Long itemId);
}
