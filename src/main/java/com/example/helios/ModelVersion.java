package com.mysite.sbb;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "model_version")
public class ModelVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_version_id")
    private Long modelVersionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(length = 45)
    private String version;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    protected ModelVersion() {}

    // getter/setter ...
    public void setModelVersionId(Long id) {
        this.modelVersionId = id;
    }
}