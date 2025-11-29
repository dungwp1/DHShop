package vn.DHShop.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class BrandRequestDTO implements Serializable {
    @NotNull(message = "name must be not null")
    private String name;

    @NotNull(message = "category_id must be not null")
    private Long categoryId;
}
