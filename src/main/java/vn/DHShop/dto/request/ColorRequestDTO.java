package vn.DHShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ColorRequestDTO implements Serializable {
    @NotNull(message = "name must be not null")
    @NotBlank(message = "name must be not blank")
    private String name;

    @NotNull(message = "hexColor must be not null")
    @NotBlank(message = "hexColor must be not blank")
    private String hexColor;


}
