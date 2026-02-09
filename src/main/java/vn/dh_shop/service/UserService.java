package vn.dh_shop.service;

import vn.dh_shop.dto.auth.*;

public interface UserService {
    RegisterResponseDTO register(RegisterRequestDTO request);
    AuthResult login(LoginRequestDTO request);
    AuthMeResponseDTO getCurrentUser();

}
