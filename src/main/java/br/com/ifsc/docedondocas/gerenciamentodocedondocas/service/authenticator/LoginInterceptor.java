package br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.authenticator;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CookieService.getCookie(request, "usuarioId") != null) {
            return true;
        }

        response.sendRedirect("/usuario/login");
        return false;
    }
}
