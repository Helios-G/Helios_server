package com.mysite.sbb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    @JsonIgnore
    private SessionEntity session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_status_code_id")
    @JsonIgnore
    private HospitalStatusCode hospitalStatusCode;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 45, name = "business_num")
    private String businessNum;

    @Column(nullable = false, length = 255)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String status;

    protected Hospital() {}

    public Hospital(String name, String businessNum, String email,
                    String password, String date, String status) {
        this.name = name;
        this.businessNum = businessNum;
        this.email = email;
        this.password = password;
        this.date = date;
        this.status = status;
    }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }

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

    public void setAdmin(Admin admin) { this.admin = admin; }
    public void setSession(SessionEntity session) { this.session = session; }
    public void setHospitalStatusCode(HospitalStatusCode code) { this.hospitalStatusCode = code; }
}
