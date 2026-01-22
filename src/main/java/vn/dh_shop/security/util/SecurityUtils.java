package vn.dh_shop.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Slf4j
public final class SecurityUtils {

    private SecurityUtils() {

    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public Long getUserId() {
        if (!isAuthenticated()) {
            log.warn("No authenticated user found");
            return null;
        }
        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof Long userId) {
            return userId;
        }
        log.warn("Principal is not userId: {}", principal);
        return null;
    }

    public String getUserRole() {
        if (!isAuthenticated()) {
            return null;
        }

        return getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse(null);
    }

}
