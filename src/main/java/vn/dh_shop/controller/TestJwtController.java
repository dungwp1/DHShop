//package vn.dh_shop.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import vn.dh_shop.dto.common.ApiResponse;
//import vn.dh_shop.exception.BadRequestException;
//import vn.dh_shop.security.jwt.JwtUtil;
//import vn.dh_shop.security.util.SecurityUtils;
//
//@RestController
//@RequestMapping("/test")
//@RequiredArgsConstructor
//@Slf4j
//public class TestJwtController {
//    private final JwtUtil jwtUtil;
//    private final SecurityUtils securityUtils;
//    @PostMapping
//    public ResponseEntity<ApiResponse<String>> testJwt (@RequestHeader("Authorization") String authorizationHeader) {
//        log.info(authorizationHeader);
//        String authHeader = authorizationHeader.split(" ")[1];
//        log.info(authHeader);
//        boolean isValid = jwtUtil.validateToken(authHeader);
//        if (!isValid) throw new BadRequestException("Token lỗi");
//        Long userId = jwtUtil.extractUserId(authHeader);
//        String role = jwtUtil.extractRole(authHeader);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ApiResponse<>(HttpStatus.OK.value()
//                        , "AUTH_SUCCESS", "UserId: "+userId+ " role: "+ role));
//    }
//
//    @GetMapping(value = "/current-user")
//    public ResponseEntity<String> testSecurityUtils () {
//        Long id = securityUtils.getUserId();
//        String role = securityUtils.getUserRole();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(role + " --- " + id);
//    }
//}
