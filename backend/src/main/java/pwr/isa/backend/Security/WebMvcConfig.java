package pwr.isa.backend.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserCustomInterceptor userCustomInterceptor;
    private final UserSpecificCustomInterceptor userSpecificCustomInterceptor;

    public WebMvcConfig(UserCustomInterceptor userCustomInterceptor, UserSpecificCustomInterceptor userSpecificCustomInterceptor) {
        this.userCustomInterceptor = userCustomInterceptor;
        this.userSpecificCustomInterceptor = userSpecificCustomInterceptor;
    }
    // every GET method is ignored
    // if token is assigned to admin, it has access to all endpoints
    // userInterceptor ensures valid token is provided for all endpoints
    // userSpecificInterceptor ensures that requests related to specific user has valid token for that user
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userCustomInterceptor)
                .addPathPatterns("/api/v1/user/**")
                .excludePathPatterns("/api/v1/users");
        registry.addInterceptor(userSpecificCustomInterceptor)
                .addPathPatterns("/api/v1/admin/**");
    }
}
