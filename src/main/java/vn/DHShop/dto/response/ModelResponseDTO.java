package vn.DHShop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id","name","brand"})

public class ModelResponseDTO {
    private Long id;
    private String name;
    private BrandResponseDTO brand;
}
