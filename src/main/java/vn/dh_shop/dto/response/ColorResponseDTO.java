package vn.dh_shop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"id", "name", "hexColor"})
public class ColorResponseDTO {
    private Long id;
    private String name;
    private String hexColor;
}
