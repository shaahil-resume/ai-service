package com.shaahil.resume.ai.exception;

import com.shaahil.resume.ai.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> exceptionMessages = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            exceptionMessages.add(error.getDefaultMessage());
        });
        ErrorResponseDto responseDto = ErrorResponseDto.builder().code(HttpStatus.BAD_REQUEST.value()).errorMessage(exceptionMessages).build();
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
