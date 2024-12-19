package pwr.isa.backend.Security.AuthSystem;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pwr.isa.backend.Security.TokenSystem.TokenService;
import pwr.isa.backend.User.User;

@Component
public class AuthUtils {

    private static TokenService tokenService;

    @Autowired
    public AuthUtils(TokenService tokenService) {
        AuthUtils.tokenService = tokenService;
    }

    public static User getUserFromToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            throw new IllegalStateException("No Authorization header found in the request");
        }
        String token = authorization.substring(7);
        return tokenService.getUserFromToken(token);
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        throw new IllegalStateException("No HTTP request found in the context");
    }
}
