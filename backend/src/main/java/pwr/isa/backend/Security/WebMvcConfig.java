package pwr.isa.backend.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserCustomInterceptor userCustomInterceptor;
    private final AdminCustomInterceptor adminCustomInterceptor;

    public WebMvcConfig(UserCustomInterceptor userCustomInterceptor,AdminCustomInterceptor adminCustomInterceptor) {
        this.userCustomInterceptor = userCustomInterceptor;
        this.adminCustomInterceptor = adminCustomInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userCustomInterceptor)
                .addPathPatterns("/api/v1/user/**");
        registry.addInterceptor(adminCustomInterceptor)
                .addPathPatterns("/api/v1/admin/**");
    }
}
