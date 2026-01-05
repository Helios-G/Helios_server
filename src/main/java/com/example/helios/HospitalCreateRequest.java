package com.mysite.sbb;

import com.fasterxml.jackson.annotation.JsonFormat;
//import java.time.LocalDate;

public class HospitalCreateRequest {

    private Long adminId;
    private Long sessionId;
    private Long hospitalStatusCodeId;

    private String name;
    private String businessNum;
    private String email;
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")   // "2025-01-01" 이런 형식 파싱
    private String date;

    private String status;

    // ★ 반드시 기본 생성자 필요
    public HospitalCreateRequest() {
    }

    // ★ Jackson이 쓸 getter/setter
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

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}