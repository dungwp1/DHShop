package vn.dh_shop.service;

import vn.dh_shop.dto.request.RegisterRequestDTO;
import vn.dh_shop.dto.response.RegisterResponseDTO;

public interface UserService {
    RegisterResponseDTO register(RegisterRequestDTO request);
}
