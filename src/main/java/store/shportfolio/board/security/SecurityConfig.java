package store.shportfolio.board.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        http.authorizeRequests(auth->{
            try {
                auth.requestMatchers(HttpMethod.POST,"/api/mail/send","/api/mail/verify").permitAll()
                        .requestMatchers("user/delete").authenticated()
                        .and()
                        .csrf(httpSecurityCsrfConfigurer ->
                                httpSecurityCsrfConfigurer.ignoringRequestMatchers("/api/**")
                                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        });

        // 2. 로그인 설정
        http.formLogin(formLogin -> formLogin
                .loginPage("/login") // 사용자 정의 로그인 페이지 경로
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true) // 로그인 성공 시 이동할 기본 페이지
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login?error=true") // 로그인 실패 시 이동할 URL
                .permitAll() // 로그인 페이지 접근 허용
        );
        // 3. 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 URL
                .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL
                .permitAll() // 로그아웃 페이지 접근 허용
        );
        // 4. 세션 관리 설정
        http.sessionManagement(sessionManagement -> sessionManagement
                .maximumSessions(1) // 최대 세션 수 제한
                .maxSessionsPreventsLogin(true) // 동일 계정의 추가 로그인 차단 (기존 세션 유지를 원하면 false)
                .expiredUrl("/login?expired=true") // 세션 만료 시 이동할 URL
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
