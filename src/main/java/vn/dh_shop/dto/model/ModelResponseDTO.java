package vn.dh_shop.dto.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.dh_shop.dto.brand.BrandResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id","name","brand"})

public class ModelResponseDTO {
    private Long id;
    private String name;
    private BrandResponseDTO brand;
}
