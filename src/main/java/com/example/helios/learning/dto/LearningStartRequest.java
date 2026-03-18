package com.example.helios.learning.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LearningStartRequest {
    private List<Long> participants;
    private int rounds;
}