package com.shaahil.resume.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDto {

    @JsonProperty("_id")
    private String mongoId;
    private String name;
    private String issuer;
    private String expiry;
    private String certificateId;
}
