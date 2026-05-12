package com.helios.user.entity;

public enum RoleType {
    ROLE_USER(1),
    ROLE_PENDING(2),
    ROLE_PARTICIPANT(3);

    private final int id;

    RoleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RoleType fromId(int id) {
        for (RoleType type : values()) {
            if (type.id == id) return type;
        }
        return ROLE_USER;
    }
}