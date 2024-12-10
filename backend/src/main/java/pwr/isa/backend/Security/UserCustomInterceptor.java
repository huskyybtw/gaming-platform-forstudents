package pwr.isa.backend.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pwr.isa.backend.Exceptions.AccessForbidenException;
import pwr.isa.backend.Exceptions.NotAuthorizedException;
import pwr.isa.backend.Security.auth.TokenService;
import pwr.isa.backend.User.User;
import pwr.isa.backend.User.UserRole;

@Component
public class UserCustomInterceptor implements HandlerInterceptor {

    private TokenService tokenService;

    public UserCustomInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        if("GET".equals(request.getMethod())){
            return true;
        }

        if (authorization == null) {
            throw new NotAuthorizedException("Token not provided");
        }

        String token = authorization.substring(7);
        User assignedUser = tokenService.getUserFromToken(token);

        if(assignedUser.getRole() == null){
            throw new NotAuthorizedException("User have no role assigned");
        }

        if(assignedUser.getRole().equals(UserRole.ADMIN)){
           return true;
        }

        if (!assignedUser.getRole().equals(UserRole.USER)) {
            throw new AccessForbidenException("User is not authorized to access this resource");
        }

        return true;
    }
}

