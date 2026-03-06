package com.mysite.sbb.dto;

public class HospitalMeResponse {
    private Long hospitalId;
    private String name;
    private String businessNum;
    private String email;
    private String date;
    private String status;

    public HospitalMeResponse(Long hospitalId, String name, String businessNum, String email, String date, String status) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.businessNum = businessNum;
        this.email = email;
        this.date = date;
        this.status = status;
    }

    public Long getHospitalId() { return hospitalId; }
    public String getName() { return name; }
    public String getBusinessNum() { return businessNum; }
    public String getEmail() { return email; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}