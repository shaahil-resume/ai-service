package com.shaahil.resume.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatLogResponse {

    private String status = "";
    private int saveCount = 0;
}
