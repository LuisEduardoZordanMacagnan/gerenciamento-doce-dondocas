package br.com.ifsc.docedondocas.gerenciamentodocedondocas.config;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.service.authenticator.SecurityFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private final SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex -> ex
                        // Só chama entrypoint quando NÃO há autenticação
                        .authenticationEntryPoint((req, res, e) -> {
                            // Só dispara 401 se não existe autenticação
                            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                res.setContentType("application/json");
                                res.getWriter().write("{\"error\": \"Não autenticado\"}");
                            } else {
                                // Usuário está autenticado → deixe a exceção seguir
                                throw e;
                            }
                        })

                        // Para erros de autorização (roles), deixe o controller agir
                        .accessDeniedHandler((req, res, e) -> {
                            // Deixe o controller ou o @ControllerAdvice tratar
                            throw e;
                        })
                )

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/logar").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/recuperar-senha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/recuperar-senha/validar").permitAll()
                        .anyRequest().authenticated())
                //.formLogin(form -> form.loginPage("/usuario/login"))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
