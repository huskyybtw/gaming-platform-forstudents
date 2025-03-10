package pwr.isa.backend.Security.AuthSystem;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import pwr.isa.backend.Exceptions.NotAuthorizedException;
import pwr.isa.backend.User.User;

import static pwr.isa.backend.Security.AuthSystem.AuthUtils.getCurrentHttpRequest;
import static pwr.isa.backend.Security.AuthSystem.AuthUtils.getUserFromToken;

@Aspect
@Component
public class EveryoneAuthorizationAspect {

    @Before("@annotation(pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne)")
    public void validateToken() {
        HttpServletRequest request = getCurrentHttpRequest();
        User user = getUserFromToken(request);

        if (user == null) {
            throw new NotAuthorizedException("U have to be logged in to do that");
        }
    }
}
