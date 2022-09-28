package com.hospyboard.api.app.user.config;

import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
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

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/forgotPassword").permitAll()
                .antMatchers(HttpMethod.POST, "/user/forgotPassword/change").permitAll()
                .antMatchers(HttpMethod.POST, "/user/changePassword").permitAll()
                .antMatchers(HttpMethod.GET, "/user/session").authenticated()
                .antMatchers(HttpMethod.GET, "/user/logout").authenticated()
                .antMatchers("/user/").hasAuthority(UserRole.ADMIN)
                .antMatchers("/user/batch/").hasAuthority(UserRole.ADMIN)

                .antMatchers("/mail/contact").permitAll()

                .antMatchers("/userToken/**").hasAuthority(UserRole.ADMIN)

                .antMatchers("/hospital/**").hasAuthority(UserRole.ADMIN)

                .antMatchers("/service/**").hasAuthority(UserRole.ADMIN)

                .antMatchers("/room/**").hasAuthority(UserRole.ADMIN)

                .anyRequest().authenticated();

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
