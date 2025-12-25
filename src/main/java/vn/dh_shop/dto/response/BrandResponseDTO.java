package vn.dh_shop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id","name","category"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrandResponseDTO {
    private Long id;
    private String name;
    private CategoryResponseDTO category;

    public BrandResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
