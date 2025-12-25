package vn.dh_shop.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;


import java.io.Serializable;

@Getter
public class CategoryRequestDTO implements Serializable {

    @NotNull(message = "field must be not null")
    private String name;




}
