package vn.dh_shop.service;

import vn.dh_shop.dto.request.auth.LoginRequestDTO;
import vn.dh_shop.dto.request.auth.RegisterRequestDTO;
import vn.dh_shop.dto.response.auth.LoginResponseDTO;
import vn.dh_shop.dto.response.auth.RegisterResponseDTO;

public interface UserService {
    RegisterResponseDTO register(RegisterRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);

}
