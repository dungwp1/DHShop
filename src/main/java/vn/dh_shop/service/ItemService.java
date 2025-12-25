package vn.dh_shop.service;

import vn.dh_shop.dto.request.ItemRequestDTO;
import vn.dh_shop.dto.response.ItemResponseDTO;
import vn.dh_shop.dto.response.PageItemsResponseDTO;

public interface ItemService {
    ItemResponseDTO addItem(ItemRequestDTO request);

    PageItemsResponseDTO getAllItem(int pageNo, int pageSize);

    ItemResponseDTO getItemById(Long itemId);

    PageItemsResponseDTO getItemByCategoryId(Long categoryId, int pageNo, int pageSize);

    PageItemsResponseDTO getItemByBrandId(Long categoryId, Long brandId, int pageNo, int pageSize);

    PageItemsResponseDTO getItemByModelId(Long categoryId);

    void deleteItem(Long itemId);
}
