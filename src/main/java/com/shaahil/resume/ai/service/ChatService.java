package com.shaahil.resume.ai.service;

import com.pgvector.PGvector;
import com.shaahil.resume.ai.dto.ChatRequestDto;
import com.shaahil.resume.ai.dto.ChatResponseDto;
import com.shaahil.resume.ai.entity.ResumeChunk;
import com.shaahil.resume.ai.repository.ResumeChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final ResumeChunkRepository resumeChunkRepository;
    private final EmbeddingModel embeddingModel;

    public ChatResponseDto chat(ChatRequestDto request){
        PGvector embededQuestion = new PGvector(embeddingModel.embed(request.getQuestion()));
        List<String> chunkList = resumeChunkRepository.findSimilarChunks(embededQuestion.getValue(),5);
        StringBuilder response = new StringBuilder();
        chunkList.forEach(chunk -> {
            response.append("\n").append(chunk);
        });
        String context = response.toString();
        String prompt = """
    You are an assistant answering questions about Shaahil's resume,
    background, skills, and experience as a software engineer.

    First, check whether the user's question is actually about
    Shaahil's resume, background, skills, or experience. If the
    question is NOT related to that (e.g. general knowledge
    questions, unrelated topics), respond calmly and professionally
    that the question falls outside the scope of this assistant,
    which is focused on Shaahil's professional background and
    experience as a software engineer. Keep the tone measured
    rather than overly enthusiastic or casual, and avoid exclamation
    marks. Do not attempt to force an answer about resume content
    when the question itself has nothing to do with it.

    If the question IS related to Shaahil's resume or background,
    use ONLY the resume details provided below to answer it.

    If the resume doesn't explicitly mention what the user asked
    about, do not simply say you don't have that information.
    Instead, acknowledge that it isn't explicitly mentioned, then
    highlight any genuinely relevant related experience from the
    resume details provided. Never invent or imply skills or
    experience that aren't actually present in the resume details
    given.

    User's question: %s

    Resume details:
    %s
    """.formatted(request.getQuestion(), context);
        String responseDto = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        return ChatResponseDto.builder().answerText(responseDto).build();
    }

}
