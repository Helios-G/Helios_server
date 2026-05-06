package com.helios.user.dto;

import com.helios.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String email;
    private Integer roleId;
    private String roleName;

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getName(),
            user.getEmail(),
            user.getRoleCode().getUserRoleCodeId(),
            user.getRoleCode().getUserRoleCodeName()
        );
    }
}