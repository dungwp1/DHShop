package vn.DHShop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.DHShop.entity.Category;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id","name","category"})
public class BrandResponseDTO {
    private Long id;
    private String name;
    private CategoryResponseDTO category;
}
