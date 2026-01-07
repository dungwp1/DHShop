package vn.dh_shop.service;

import vn.dh_shop.dto.auth.LoginRequestDTO;
import vn.dh_shop.dto.auth.RegisterRequestDTO;
import vn.dh_shop.dto.auth.LoginResponseDTO;
import vn.dh_shop.dto.auth.RegisterResponseDTO;

public interface UserService {
    RegisterResponseDTO register(RegisterRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);

}
