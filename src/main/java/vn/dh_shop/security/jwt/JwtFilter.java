package vn.dh_shop.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//        Đọc header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
//            Không có token (có thể là API Public -> cho qua -> SecurityConfig sẽ xử lý chặn hay không)
            filterChain.doFilter(request, response);
            return;
        }
        if (!authorizationHeader.startsWith("Bearer ")) {
//            có token nhưng không đúng bearer -> tạm thời cho qua
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        boolean isValid = jwtUtil.validateToken(token);
        if (!isValid) {
            request.setAttribute("auth_error", "TOKEN_INVALID");
            throw new AuthenticationException("Token invalid") {};
        }

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
