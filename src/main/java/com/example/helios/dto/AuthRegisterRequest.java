package com.mysite.sbb.dto;

public class AuthRegisterRequest {
    private Long adminId;
    private Long sessionId;
    private Long hospitalStatusCodeId;

    private String name;
    private String businessNum;
    private String email;
    private String password;

    public AuthRegisterRequest() {}

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getHospitalStatusCodeId() { return hospitalStatusCodeId; }
    public void setHospitalStatusCodeId(Long hospitalStatusCodeId) { this.hospitalStatusCodeId = hospitalStatusCodeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBusinessNum() { return businessNum; }
    public void setBusinessNum(String businessNum) { this.businessNum = businessNum; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}