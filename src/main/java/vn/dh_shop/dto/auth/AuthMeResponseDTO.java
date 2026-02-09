package vn.dh_shop.dto.auth;

import lombok.Getter;
import lombok.Setter;
import vn.dh_shop.entity.enums.Role;
@Getter
@Setter
public class AuthMeResponseDTO {
    private Long id;
    private String username;
    private Role role;
}
