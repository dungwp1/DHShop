package vn.dh_shop.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class RamRequestDTO implements Serializable {
    @NotNull(message = "name must be not null")
    private String name;
}
