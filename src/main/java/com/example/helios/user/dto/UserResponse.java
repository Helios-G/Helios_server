package com.example.helios.user.dto;

import com.example.helios.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String name;
    private String businessNum;
    private LocalDate createdAt; // 엔티티의 date 필드 매핑

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getEmail(),
            user.getName(),
            user.getBusinessNum(),
            user.getCreatedAt().toLocalDate()
        );
    }
}