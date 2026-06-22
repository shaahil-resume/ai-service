package com.shaahil.resume.ai.dto;

import com.shaahil.resume.ai.util.ResumeSection;
import lombok.Data;

import java.util.List;

@Data
public class SectionUpdateRequest {

    private ResumeSection sectionType;
    private List<SectionUpdateItem> content;
}
