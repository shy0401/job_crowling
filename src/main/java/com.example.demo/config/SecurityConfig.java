import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login", "/public/**").permitAll() // 회원가입, 로그인, 공개 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated() // 다른 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login") // 기본 로그인 페이지 경로
                        .defaultSuccessUrl("/home", true) // 로그인 성공 시 리다이렉트 경로
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 엔드포인트
                        .logoutSuccessUrl("/login?logout") // 로그아웃 성공 후 리다이렉트 경로
                        .permitAll()
                );
        return http.build();
    }
}
