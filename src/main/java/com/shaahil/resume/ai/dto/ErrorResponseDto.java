package com.shaahil.resume.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponseDto {

    private int code;
    private List<String> errorMessage;
}
