package com.shaahil.resume.ai.client;

import com.shaahil.resume.ai.dto.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "profile-service",
url = "${profile.service.url}")
public interface ProfileServiceClient {

    @GetMapping("/api/profile")
    ProfileResponse getResume();

}
