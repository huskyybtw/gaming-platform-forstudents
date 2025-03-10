package pwr.isa.backend.Security.AuthSystem;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pwr.isa.backend.Exceptions.NotAuthorizedException;
import pwr.isa.backend.GameSystem.GameService;
import pwr.isa.backend.Posters.TeamPosters.TeamPosterService;
import pwr.isa.backend.Posters.UserPosters.UserPosterService;
import pwr.isa.backend.Team.TeamService;
import pwr.isa.backend.User.User;
import pwr.isa.backend.User.UserRole;
import pwr.isa.backend.User.UserService;

@Aspect
@Component
public class AuthorizationAspect {

    private final TeamService teamService;
    private final UserService userService;
    private final UserPosterService userPosterService;
    private final TeamPosterService teamPosterService;
    private final GameService gameService;

    @Autowired
    public AuthorizationAspect(TeamService teamService,
                               UserService userService,
                               UserPosterService userPosterService,
                               TeamPosterService teamPosterService,
                               GameService gameService) {
        this.teamService = teamService;
        this.userService = userService;
        this.userPosterService = userPosterService;
        this.teamPosterService = teamPosterService;
        this.gameService = gameService;
    }


    @Pointcut("@annotation(pwr.isa.backend.Security.AuthSystem.Authorize)")
    public void checkAuthMethods() {}

    @Before("@annotation(pwr.isa.backend.Security.AuthSystem.Authorize) && args(.., id)")
    public void checkAuthorization(JoinPoint joinPoint, Long id) {
        HttpServletRequest request = AuthUtils.getCurrentHttpRequest();
        User assignedUser = AuthUtils.getUserFromToken(request);

        String requestUri = request.getRequestURI();
        if (assignedUser.getRole().equals(UserRole.ADMIN)) {
            return;
        }

        switch (getResourceType(requestUri)) {
            case "teams":
                handleTeamAuthorization(assignedUser, id);
                break;
            case "users":
                handleUserAuthorization(assignedUser, id);
                break;
            case "userPosters":
                handleUserPosterAuthorization(assignedUser, id);
                break;
            case "teamPosters":
                handleTeamPosterAuthorization(assignedUser, id);
                break;
            default:
                throw new NotAuthorizedException("Unsupported resource type");
        }
    }

    private void handleTeamPosterAuthorization(User assignedUser, Long id) {
        Long teamId = teamPosterService.getTeamPosterById(id).getTeamId();
        Long posterOwnerId = teamService.getTeamById(teamId).getTeam().getTeamCaptain();
        if (!assignedUser.getID().equals(posterOwnerId)) {
            throw new NotAuthorizedException("User is not authorized to access this team poster resource");
        }
    }

    private void handleUserPosterAuthorization(User assignedUser, Long id) {
        Long posterOwnerId = userPosterService.getUserPosterById(id).getUserId();;
        if (!assignedUser.getID().equals(posterOwnerId)) {
            throw new NotAuthorizedException("User is not authorized to access this user poster resource");
        }
    }

    private void handleTeamAuthorization(User assignedUser, Long teamId) {
        Long teamCaptainId = teamService.getTeamById(teamId).getTeam().getTeamCaptain();
        if (!assignedUser.getID().equals(teamCaptainId)) {
            throw new NotAuthorizedException("User is not authorized to access this team resource");
        }
    }

    private void handleUserAuthorization(User assignedUser, Long userId) {
        if (!assignedUser.getID().equals(userId)) {
            throw new NotAuthorizedException("User is not authorized to access this user resource");
        }
    }

    private String getResourceType (String requestUri){
        if (requestUri.startsWith("/api/v1/teams")) {
            return "teams";
        } else if (requestUri.startsWith("/api/v1/users")) {
            return "users";
        }
        else if (requestUri.startsWith("/api/v1/players")) {
            return "users";
        }
        else if (requestUri.startsWith("/api/v1/posters/user")) {
            return "userPosters";
        }
        else if (requestUri.startsWith("/api/v1/posters/team")) {
            return "teamPosters";
        }

        return "unknown";
    }
}
