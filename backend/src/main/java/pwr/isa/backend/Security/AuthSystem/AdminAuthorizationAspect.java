package pwr.isa.backend.Security.AuthSystem;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import pwr.isa.backend.Exceptions.NotAuthorizedException;
import pwr.isa.backend.User.User;
import pwr.isa.backend.User.UserRole;

import static pwr.isa.backend.Security.AuthSystem.AuthUtils.getCurrentHttpRequest;
import static pwr.isa.backend.Security.AuthSystem.AuthUtils.getUserFromToken;

@Aspect
@Component
public class AdminAuthorizationAspect {

    @Before("@annotation(pwr.isa.backend.Security.AuthSystem.AuthorizeAdminOnly)")
    public void checkAdminAuthorization() {
        HttpServletRequest request = getCurrentHttpRequest();
        User user = getUserFromToken(request);

        if (user == null || user.getRole() == null || !user.getRole().equals(UserRole.ADMIN)) {
            throw new NotAuthorizedException("User is not authorized to access this resource");
        }
    }

}
