package vn.dh_shop.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtFilter called for URI: {}", request.getRequestURI());
        String token = null;
        // ===== 1️⃣ ƯU TIÊN COOKIE (WEB) =====
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        // ===== 2️⃣ FALLBACK BEARER (MOBILE / API) =====
        if (token == null) {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
        }
        // ===== 3️⃣ KHÔNG CÓ TOKEN → CHO QUA =====
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // ===== 4️⃣ VALIDATE TOKEN =====
        boolean isValid = jwtUtil.validateToken(token);
        if (!isValid) {
            // 🔥 XÓA COOKIE KHI TOKEN KHÔNG HỢP LỆ
            ResponseCookie cookie = ResponseCookie.from("access_token", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            request.setAttribute("auth_error", "TOKEN_INVALID");
            throw new AuthenticationException("Token invalid") {};
        }
        // ===== 5️⃣ EXTRACT INFO =====
        Long userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userId,null, List.of(authority));

        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);

    }
}
