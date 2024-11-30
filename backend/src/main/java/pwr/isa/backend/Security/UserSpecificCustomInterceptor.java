package pwr.isa.backend.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import pwr.isa.backend.Exceptions.AccessForbidenException;
import pwr.isa.backend.Exceptions.NotAuthorizedException;
import pwr.isa.backend.Security.auth.TokenService;
import pwr.isa.backend.User.User;
import pwr.isa.backend.User.UserRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class UserSpecificCustomInterceptor implements HandlerInterceptor {

    private TokenService tokenService;

    public UserSpecificCustomInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String customHeader = request.getHeader("auth");

        if("GET".equals(request.getMethod())){
            return true;
        }

        if (customHeader == null) {
            throw new NotAuthorizedException("Token not provided");
        }

        User assignedUser = tokenService.checkToken(customHeader);

        if(assignedUser.getRole() == null){
            throw new NotAuthorizedException("User have no role assigned");
        }

        if(assignedUser.getRole().equals(UserRole.ADMIN)){
            return true;
        }

        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long pathId = null;
        if (pathVariables != null && pathVariables.containsKey("id")) {
            try {
                // Parse the ID as a Long
                pathId = Long.parseLong(pathVariables.get("id"));
            } catch (NumberFormatException e) {
                throw new AccessForbidenException("Incorrect ID format");
            }
        }

        if(!Objects.equals(pathId, assignedUser.getID())){
            throw new AccessForbidenException("User is not authorized to access this resource");
        }

        return true;
    }
}

