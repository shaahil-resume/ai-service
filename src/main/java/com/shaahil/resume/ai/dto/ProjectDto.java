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
public class ProjectDto {

    @JsonProperty("_id")
    private String mongoId;
    private String title;
    private List<String> description;
    private String github;
    private List<String> tech;
    private String from;
    private String to;

}
