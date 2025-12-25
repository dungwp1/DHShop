package vn.dh_shop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"id", "categorId", "categoryName", "brandId", "brandName"
        , "modelId", "modelName", "ramId", "ramName", "storageId", "storageName"
        , "colorId", "colorName", "hexColor", "price", "description", "imageURLs"})
public class ItemResponseDTO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private Long modelId;
    private String modelName;
    private Long ramId;
    private String ramName;
    private Long storageId;
    private String storageName;
    private Long colorId;
    private String colorName;
    private String hexColor;
    private Long price;
    private String description;
    private List<String> imageURLs;
    

}
