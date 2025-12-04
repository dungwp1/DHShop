package vn.DHShop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"id", "name", "hexColor"})
public class ColorResponseDTO {
    private Long id;
    private String name;
    private String hexColor;
}
