package com.shaahil.resume.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatLogRequestDto {

    private String conversationId;
    private String email;
    private List<QnPair> conversation;
}
