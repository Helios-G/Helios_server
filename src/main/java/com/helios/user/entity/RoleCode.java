package com.helios.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user_role_code")
@Getter
@NoArgsConstructor
public class RoleCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_code_id")
    private Integer userRoleCodeId;

    @Column(name = "user_role_code_name", nullable = false, length = 45)
    private String userRoleCodeName;

    public RoleCode(String userRoleCodeName) {
        this.userRoleCodeName = userRoleCodeName;
    }
}