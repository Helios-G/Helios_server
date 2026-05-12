package com.helios.user.service;

import com.helios.user.dto.UserResponse;
import com.helios.user.entity.User;
import com.helios.user.exception.UserNotFoundException;
import com.helios.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원 정보 조회
    public UserResponse getMyInfo(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
        return UserResponse.from(user);
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        user.changePassword(passwordEncoder.encode(newPassword));
    }

    // 회원 탈퇴 (soft delete)
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        user.withdraw();
    }
}