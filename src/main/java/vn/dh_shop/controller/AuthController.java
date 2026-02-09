package vn.dh_shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.dh_shop.dto.auth.*;
import vn.dh_shop.dto.common.ApiResponse;
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
    public ResponseEntity<ApiResponse<WebLoginResponseDTO>> loginUser (@Valid @RequestBody LoginRequestDTO request, HttpServletResponse response) {
        AuthResult authResult = userService.login(request);

        String jwt = authResult.getAccessToken();

        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                        .httpOnly(true)
                        .path("/")
                        .sameSite("Lax")
                        .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "LOGIN_SUCCESS", authResult.getUser()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)          // 🔥 xóa cookie
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<AuthMeResponseDTO>> getCurrentUser() {
        AuthMeResponseDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(
                new ApiResponse<>(200, "GET_ME_SUCCESS", user)
        );
    }

}

