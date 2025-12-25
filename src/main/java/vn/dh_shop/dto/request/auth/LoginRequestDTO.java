package vn.dh_shop.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class LoginRequestDTO  implements Serializable {
    @NotBlank(message = "email must be not blank")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "password must be not blank")
    private String password;
}
