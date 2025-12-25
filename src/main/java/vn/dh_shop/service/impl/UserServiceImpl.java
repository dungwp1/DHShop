package vn.dh_shop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.dh_shop.dto.request.RegisterRequestDTO;
import vn.dh_shop.dto.response.RegisterResponseDTO;
import vn.dh_shop.entity.User;
import vn.dh_shop.entity.enums.Role;
import vn.dh_shop.entity.enums.UserStatus;
import vn.dh_shop.exception.BadRequestException;
import vn.dh_shop.repository.UserRepository;
import vn.dh_shop.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO request) {
//        check exists Email
        boolean isExistsEmail = userRepository.existsByEmail(request.getEmail());
        if (isExistsEmail) throw new BadRequestException("Email đã tồn tại trong hệ thống");
//        Tạo username
        String email = request.getEmail().toLowerCase();
        String userName = email.substring(0, email.indexOf("@"));
//        Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());
//        Tạo entity để lưu vào db
        User user = new User();
        user.setEmail(email);
        user.setUserName(userName);
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
}
