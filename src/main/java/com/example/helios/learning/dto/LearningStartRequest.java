package com.example.helios.learning.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  // 파라미터 없는 생성자 (JSON 직렬화용)
@AllArgsConstructor // 모든 필드를 포함하는 생성자 (서비스에서 호출용)
public class LearningStartRequest {
    private List<Long> participants;
    private int rounds;
}