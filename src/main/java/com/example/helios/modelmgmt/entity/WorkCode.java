package com.example.helios.modelmgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_work_code")
@Getter
@Setter
public class WorkCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workCodeId;

    @Column(length = 45, nullable = false)
    private String workCodeName;

    public enum Type {
        BEFORE_TRAINING(0),
        TRAINING(1),
        DEPLOYING(2),
        TRAINING_FAILED(3);

        private final int id;
        Type(int id) { this.id = id; }
        public int getId() { return id; }
    }
}