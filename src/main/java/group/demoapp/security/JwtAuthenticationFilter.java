package group.demoapp.security;

import group.demoapp.service.SecurityUserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final SecurityUserService securityUserService;
    private static final AccountPenaltyCounter accountPenaltyCounter = new AccountPenaltyCounter();


    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private static final int maximumAccountPenalties = 3;

    static class AccountPenaltyCounter {
        private final Map<String, Integer> accountPenalties = new HashMap<>();

        /*
         * returns count of unauthorized accesses by username */
        private int addAccountPenalty(String username) {
            if (!accountPenalties.containsKey(username)) {
                accountPenalties.put(username, 1);
            } else {
                accountPenalties.put(username, accountPenalties.get(username) + 1);
            }
            return accountPenalties.get(username);
        }

        private void resetAccountPenalties(String username) {
            accountPenalties.remove(username);
        }

        private int getAccountPenalties(String username) {
            if (!accountPenalties.containsKey(username)) {
                return 0;
            } else {
                return accountPenalties.get(username);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Шаг 1: Извлечение заголовка авторизации из запроса
        String authorizationHeader = request.getHeader(HEADER_NAME);
        String username = null;

        //  Шаг 2: Проверка наличия заголовка авторизации
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            // Шаг 3: Извлечение токена из заголовка
            String jwtToken = authorizationHeader.substring(BEARER_PREFIX.length());

            try {
                // Шаг 4: Извлечение имени пользователя из JWT токена
                username = jwtUtils.extractUsername(jwtToken);

                // Шаг 5: Проверка валидности токена и аутентификации
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = securityUserService.loadUserByUsername(username);

                    jwtUtils.isTokenValid(jwtToken, user);

                    // Шаг 6: Создание нового контекста безопасности
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);

                }
            } catch (ExpiredJwtException e){
                username = e.getClaims().getSubject();
                int penaltyCount = accountPenaltyCounter.addAccountPenalty(username);
                if (penaltyCount >= maximumAccountPenalties) {
                    User user = securityUserService.loadUserByUsername(username);
                    user.setAccountNonBlocked(false);
                    throw new AccessDeniedException("Account blocked");
                }
                throw e;
            }
        }
        // Шаг 7: Передача запроса на дальнейшую обработку в фильтрующий цепочке
        filterChain.doFilter(request, response);
    }

}
