package ch.admin.astra.vz.lc.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    @NotNull
    private RequestMatcher publicRoutesMatcher() {
        // all routes are public
        return new AntPathRequestMatcher("/**");
    }

    @Bean
    @Order(100)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // allow
        RequestMatcher publicRoutes = publicRoutesMatcher();
        http.securityMatcher(publicRoutes)
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable); // Disable CSRF since no authentication needed in lc;

        return http.build();
    }

}