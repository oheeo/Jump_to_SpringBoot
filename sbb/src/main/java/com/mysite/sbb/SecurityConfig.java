package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
// prePostEnabled = true : QuestionController와 AnswerController에서 로그인 여부를 판별하기 위해 사용했던 @PreAuthorize 애너테이션을 사용하기 위해 반드시 필요
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().requestMatchers(
                new AntPathRequestMatcher("/**")).permitAll()
                .and()
                .csrf().ignoringRequestMatchers(
                        new AntPathRequestMatcher("/h2-console/**"))
                .and()
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))

                // 로그인 구현 (스프링 시큐리티의 로그인 설정 담당)
                .and()
                    .formLogin()
                    .loginPage("/user/login")  // 로그인 페이지 URL
                    .defaultSuccessUrl("/")  // 성공시 이동하는 디폴트 페이지는 루트 URL

                // 로그아웃 구현
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)

                ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    // AuthenticationManager 빈 생성
    // AuthenticationManager : 스프링 시큐리티의 인증을 담당
    // 사용자 인증시 앞에서 작성한 UserSecurityService와 PasswordEncoder를 사용

}
