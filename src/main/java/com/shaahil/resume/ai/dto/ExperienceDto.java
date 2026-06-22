package com.shaahil.resume.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDto {

    @JsonProperty("_id")
    private String mongoId;
    private String company;
    private String role;
    private String location;
    private String from;
    private String to;
    private List<String> bullets;
    private List<String> tech;
}
