package edu.sena.finance.track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserAuthorizationInterceptor userAuthorizationInterceptor;

    @Autowired
    public WebMvcConfig(UserAuthorizationInterceptor userAuthorizationInterceptor) {
        this.userAuthorizationInterceptor = userAuthorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuthorizationInterceptor)
                .excludePathPatterns("/error", "/", "/logout", "/oauth2/authorization/okta"); // Excluir rutas específicas si es necesario
    }
}
