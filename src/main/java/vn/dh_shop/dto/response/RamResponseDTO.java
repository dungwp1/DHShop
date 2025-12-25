package vn.dh_shop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "name"})
public class RamResponseDTO {
    private Long id;
    private String name;
}
