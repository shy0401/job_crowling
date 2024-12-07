package com.example.demo.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 예제 사용자 하드코딩 (실제 앱에서는 DB에서 사용자 정보를 가져옵니다)
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password("{noop}password") // {noop}: 비밀번호 암호화 비활성화
                    .roles("USER") // 사용자 역할
                    .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
