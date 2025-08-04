package edu.sena.finance.track;

import edu.sena.finance.track.entities.User;
import edu.sena.finance.track.entities.enums.Status;
import edu.sena.finance.track.repositories.UserRepository;
import edu.sena.finance.track.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class UserAuthorizationInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserAuthorizationInterceptor(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // Ignorar la ruta de error para evitar bucle
        if (request.getRequestURI().equals("/error")) {
            return true;
        }

        // Obtener el usuario autenticado de Auth0
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si no hay autenticación, continuar con el flujo normal
        if (authentication == null ||
                authentication instanceof AnonymousAuthenticationToken) {
            response.sendRedirect("/");
            return false;
        }

        // Obtener el email del usuario autenticado
        OidcUser principal = (OidcUser) authentication.getPrincipal();
        User user = userService.get(principal);

        if (!userRepository.existsByEmail(principal.getEmail())) {
            log.warn("Unauthorized access attempt by user: {}", principal.getEmail());
            response.sendRedirect("/error");
            return false;
        }
        if (user.getStatus() == Status.SUSPENDED) {
            log.warn("Unauthorized access attempt by user: {}, with status {}", principal.getEmail(), Status.SUSPENDED);
            response.sendRedirect("/error");
            return false;
        }

        return true;
    }
}