package vn.DHShop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"currentPage", "totalPages", "totalItems", "items"})
public class PageItemsResponseDTO {
    private int currentPage;
    private int totalPages;
    private Long totalItems;
    List<ItemResponseDTO> items;
}
