package vn.dh_shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dh_shop.dto.request.auth.LoginRequestDTO;
import vn.dh_shop.dto.request.auth.RegisterRequestDTO;
import vn.dh_shop.dto.response.ApiResponse;
import vn.dh_shop.dto.response.auth.LoginResponseDTO;
import vn.dh_shop.dto.response.auth.RegisterResponseDTO;
import vn.dh_shop.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> registerUser (@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = userService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(),"REGISTER_SUCCESS", response));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> loginUser (@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "LOGIN_SUCCESS", response));
    }
}
