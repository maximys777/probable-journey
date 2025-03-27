package me.maximys777.Video.config;

import me.maximys777.Video.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final UserService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;

    @Autowired
    public SecurityConfig(UserService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/SAO/auth/**").permitAll()
                        .requestMatchers("/SAO/user/**").permitAll()
                        .requestMatchers("/SAO/category/**").permitAll()
                        .requestMatchers("/SAO/video/**").permitAll()
                        .requestMatchers("/SAO/video/{id}/comments/**").permitAll()

                        // ДЛЯ ФРОНТА
//                        .requestMatchers(HttpMethod.GET,"/SAO/user/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/SAO/category/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/SAO/category/**").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/SAO/category/**").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/SAO/category/**").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/SAO/video/{id}/comments").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/SAO/video/{id}/comments").hasAnyAuthority("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/SAO/video/{id}/comments").hasAuthority("USER, ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/SAO/video/{id}/comments").hasAuthority("USER, ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/SAO/video/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/SAO/video/**").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/SAO/video/**").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/SAO/video/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
