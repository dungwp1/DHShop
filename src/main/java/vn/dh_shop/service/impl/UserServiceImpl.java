package vn.dh_shop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.auth.LoginRequestDTO;
import vn.dh_shop.dto.request.auth.RegisterRequestDTO;
import vn.dh_shop.dto.response.auth.LoginResponseDTO;
import vn.dh_shop.dto.response.auth.RegisterResponseDTO;
import vn.dh_shop.entity.User;
import vn.dh_shop.entity.enums.Role;
import vn.dh_shop.entity.enums.UserStatus;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.UserRepository;
import vn.dh_shop.security.jwt.JwtUtil;
import vn.dh_shop.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        //        Set lại email và username
        String email = request.getEmail().toLowerCase();
        String username = email.substring(0, email.indexOf("@"));
//        check exists Email
        boolean isExistsEmail = userRepository.existsByEmail(email);
        if (isExistsEmail) throw new BadRequestException("Email đã tồn tại trong hệ thống");

//        Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());
//        Tạo entity để lưu vào db
        User user = new User();
        user.setEmail(email);
        user.setUserName(username);
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
//        Tạo responseDTO
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUserName());

        return response;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
//        Set lại email
        String email = request.getEmail().toLowerCase();
//        Find by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new BadRequestException("Email hoặc Password không hợp lệ"));
//        Check status
        if (user.getStatus() == UserStatus.BLOCKED) throw new BadRequestException("Tài khoản đã bị khóa");
//        Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new BadRequestException("Email hoặc Password không hợp lệ");
//        Tạo jwt
        String token = jwtUtil.generateToken(user.getId(),user.getRole().name());
//        Tạo responseDTO
        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUserName());
        response.setAccessToken(token);
        return response;
    }
}
