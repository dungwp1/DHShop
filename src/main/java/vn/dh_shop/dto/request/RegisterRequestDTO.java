package vn.dh_shop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class RegisterRequestDTO implements Serializable {
    @NotBlank(message = "email must be not blank")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "password must be not blank")
    @Size(min = 6, message = "password must be at least 6 characters")
    private String password;
}
