package com.shaahil.resume.ai.controller;

import com.shaahil.resume.ai.dto.ChatRequestDto;
import com.shaahil.resume.ai.dto.ChatResponseDto;
import com.shaahil.resume.ai.repository.ResumeChunkRepository;
import com.shaahil.resume.ai.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final ResumeChunkRepository resumeChunkRepository;
    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDto> chat(@Valid @RequestBody ChatRequestDto chatRequest) {
        return ResponseEntity.ok(chatService.chat(chatRequest));
    }

}
