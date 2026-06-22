package com.shaahil.resume.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponseDto {
    private String answerText;
}
