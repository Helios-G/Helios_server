package com.mysite.sbb;
import jakarta.persistence.*;

@Entity
@Table(name = "session")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @Column(length = 45)
    private String title;

    @Column(length = 45)
    private String description;

    @Column(length = 45)
    private String participation;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "data_formant", length = 45)
    private String dataFormant;

    @Column(name = "label_class_list", length = 45)
    private String labelClassList;

    @Column(name = "label_class_count")
    private Integer labelClassCount;
    
    @Column(length = 3)
    private Integer status;

    @Column(name = "created_by", length = 45)
    private String createdBy;

    @Column(name = "created_at", length = 45)
    private String createdAt;

    @Column(name = "session_end_at", length = 45)
    private String sessionEndAt;

    protected SessionEntity() {}
    
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    // getter/setter 생략 가능 (필요한 것만 생성)
    public void setSessionId(Long id) {
        this.sessionId = id;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}