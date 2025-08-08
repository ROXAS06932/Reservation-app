package com.example.reservation_app.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.reservation_app.model.User;
import com.example.reservation_app.repository.UserRepository;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers( "/signup", "/signin", "/register", "/css/**", "/js/**", "/h2-console/**").permitAll() // H2コンソールを許可
                .requestMatchers("/admin/**").hasRole("ADMIN") //管理者専用
                .requestMatchers("/user/**").hasRole("USER") //ユーザー専用
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
            .loginPage("/signin") //独自のログインページを使う場合
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/home", true) // ログイン成功後のリダイレクト先
            .failureUrl("/signin?error") // ログイン失敗時のリダイレクト先
            .permitAll() // ログインページは誰でもアクセス可能
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // H2コンソールのCSRF保護を無効化
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .logout(logout -> logout
            .logoutUrl("/logout") // ログアウトのURL
            .logoutSuccessUrl("/signin") // ログアウト成功後のリダイレクト先
            .invalidateHttpSession(true) // セッションを無効化
            .deleteCookies("JSESSIONID")); // クッキー削除
            
        return http.build();
    }

    @Bean
    public CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setEnabled(true);
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ADMIN");
                repo.save(admin);
            }
        };
    }

}
