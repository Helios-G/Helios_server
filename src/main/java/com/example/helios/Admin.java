package com.mysite.sbb;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(length = 45)
    private String name;

    @Column(length = 45)
    private String email;

    @Column(length = 100)
    private String password;

    protected Admin() {}

    // getter/setter ...

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long id) { this.adminId = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}