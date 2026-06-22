package com.shaahil.resume.ai.dto;

import com.shaahil.resume.ai.util.OperationType;
import lombok.Data;

import java.util.Map;

@Data
public class SectionUpdateItem {
    private String sectionId;
    private OperationType operation;
    private Map<String, Object> items;
}
