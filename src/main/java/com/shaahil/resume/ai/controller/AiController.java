package com.shaahil.resume.ai.controller;

import com.shaahil.resume.ai.dto.ChatLogRequestDto;
import com.shaahil.resume.ai.dto.ChatLogResponse;
import com.shaahil.resume.ai.dto.ChatRequestDto;
import com.shaahil.resume.ai.dto.ChatResponseDto;
import com.shaahil.resume.ai.entity.ChatLog;
import com.shaahil.resume.ai.repository.ChatLogRepository;
import com.shaahil.resume.ai.repository.ResumeChunkRepository;
import com.shaahil.resume.ai.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final ChatService chatService;


    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDto> chat(@Valid @RequestBody ChatRequestDto chatRequest) {
        return ResponseEntity.ok(chatService.chat(chatRequest));
    }

    @GetMapping("/logs")
    public ResponseEntity<Page<ChatLog>> getLogs(
            @RequestParam String email,
            Pageable pageable) {
        return ResponseEntity.ok(chatService.getLogs(email,pageable));
    }

    @PostMapping("/save-chat")
    public ResponseEntity<ChatLogResponse> saveChat(@RequestBody ChatLogRequestDto chatLogRequest) {
        return ResponseEntity.ok(chatService.saveChatLog(chatLogRequest));
    }

}
