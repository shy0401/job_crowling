package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 데이터베이스에서 사용자 정보 가져오기
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        // User를 UserDetails로 변환하여 반환
        return new UserDetailsAdapter(userOptional.get());
    }

    // 내부 클래스 UserDetailsAdapter
    private static class UserDetailsAdapter implements UserDetails {

        private final User user;

        public UserDetailsAdapter(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // 사용자의 권한을 설정 (예제에서는 비어 있음)
            return null;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true; // 계정 만료 로직 적용 가능
        }

        @Override
        public boolean isAccountNonLocked() {
            return true; // 계정 잠금 로직 적용 가능
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true; // 인증 정보 만료 로직 적용 가능
        }

        @Override
        public boolean isEnabled() {
            return true; // 계정 활성화 로직 적용 가능
        }
    }
}
