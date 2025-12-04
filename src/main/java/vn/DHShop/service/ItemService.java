package vn.DHShop.service;

import vn.DHShop.dto.request.ItemRequestDTO;
import vn.DHShop.dto.response.ItemResponseDTO;

import java.util.List;

public interface ItemService {
    ItemResponseDTO addItem(ItemRequestDTO request);

    List<ItemResponseDTO> getAllItem();

    ItemResponseDTO getItemById(Long itemId);

    void deleteItem(Long itemId);
}
