package com.example.helios.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_user_role_code")
@Getter
@Setter
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

    public enum Type {
        ROLE_USER(1),
        ROLE_PENDING(2),
        ROLE_PARTICIPANT(3);

        private final int id;
        Type(int id) { this.id = id; }
        public int getId() { return id; }
    }
}