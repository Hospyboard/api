package com.hospyboard.api.app.user.config;

import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final UserService userService;
    private final JwtTokenFilter jwtTokenFilter;

    public WebSecurity(JwtTokenFilter jwtTokenFilter,
                       UserService userService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.userService = userService;

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http.cors().and().csrf().disable();

        //Set unauthorized requests exception handler
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        )
                )
                .and();

        http.authorizeHttpRequests(exchanges -> exchanges

                .requestMatchers(
                        new AntPathRequestMatcher("/user/register**"),
                        new AntPathRequestMatcher("/user/login**"),
                        new AntPathRequestMatcher("/user/login**"),
                        new AntPathRequestMatcher("/user/forgotPassword**"),
                        new AntPathRequestMatcher("/user/forgotPassword/change**"),
                        new AntPathRequestMatcher("/user/changePassword**")
                ).permitAll()

                .requestMatchers(
                        new AntPathRequestMatcher("/user/session**"),
                        new AntPathRequestMatcher("/user/logout**")
                ).authenticated()

                .requestMatchers(
                        new AntPathRequestMatcher("/user**"),
                        new AntPathRequestMatcher("/user/batch**")
                ).hasAuthority(UserRole.ADMIN)

                .requestMatchers("/mail/contact**").permitAll()

                .requestMatchers("/userToken**").hasAuthority(UserRole.ADMIN)

                .requestMatchers("/hospital**").hasAuthority(UserRole.ADMIN)

                .requestMatchers("/service**").hasAuthority(UserRole.ADMIN)

                .requestMatchers("/room**").hasAuthority(UserRole.ADMIN)

                .anyRequest().authenticated()
        ).httpBasic();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new HospyboardApiAuth(userService);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}
