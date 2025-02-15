package group.demoapp.security;

import group.demoapp.service.SecurityUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final SecurityUserService securityUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Шаг 1: Извлечение заголовка авторизации из запроса
        String authorizationHeader = request.getHeader("Authorization");
        String username = null;

        //  Шаг 2: Проверка наличия заголовка авторизации
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Шаг 3: Извлечение токена из заголовка
            String jwtToken = authorizationHeader.substring(7); // "Bearer " имеет длину 7

            // Шаг 4: Извлечение имени пользователя из JWT токена
            username = jwtUtils.extractUsername(jwtToken);

            // Шаг 5: Проверка валидности токена и аутентификации
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = securityUserService.loadUserByUsername(username);

                if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
