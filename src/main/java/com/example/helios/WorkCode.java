package com.mysite.sbb;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_work_code")
public class WorkCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_code_id")
    private Long workCodeId;

    @Column(name = "work_code_name", length = 45)
    private String workCodeName;

    protected WorkCode() {}

    // getter/setter ...
    public void setWorkCodeId(Long id) {
        this.workCodeId = id;
    }
}