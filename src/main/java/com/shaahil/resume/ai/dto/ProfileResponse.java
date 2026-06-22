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
public class ProfileResponse {

    @JsonProperty("_id")
    private String mongoId;
    private String name;
    private String title;
    private String location;
    private String email;
    private String phone;
    private String github;
    private String linkedin;
    private String summary;
    private List<ExperienceDto> experience;
    private List<EducationDto> education;
    private List<CertificationDto> certification;
    private List<ProjectDto> projects;
    private List<SkillsDto> skills;
}
