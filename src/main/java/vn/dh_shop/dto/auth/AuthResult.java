package vn.dh_shop.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResult {
    private WebLoginResponseDTO user;
    private String accessToken;
}
