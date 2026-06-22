package com.shaahil.resume.ai.config;

import com.shaahil.resume.ai.service.StartupInjectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StartupIngestionRunner implements ApplicationRunner {

    private final StartupInjectionService startupInjectionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        startupInjectionService.ingest();
    }
}
