package com.kjung.springoauth.core.security.config;

import com.kjung.springoauth.core.security.oAuth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/signup",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/.well-known/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                       .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .failureHandler(((request, response, exception) -> {
                            if (exception instanceof OAuth2AuthenticationException auth) {
                                if (auth.getError().getErrorCode().equals("need_register")) {
                                    response.sendRedirect("/signup"); // 추가 정보 입력 페이지로 이동
                                    return;
                                }
                            }

                            response.sendRedirect("/login?error");
                        }))
                )
                .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }

}
