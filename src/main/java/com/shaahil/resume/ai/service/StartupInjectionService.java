package com.shaahil.resume.ai.service;


import com.pgvector.PGvector;
import com.shaahil.resume.ai.client.ProfileServiceClient;
import com.shaahil.resume.ai.dto.*;
import com.shaahil.resume.ai.entity.ResumeChunk;
import com.shaahil.resume.ai.repository.ResumeChunkRepository;
import com.shaahil.resume.ai.util.ResumeSection;
import lombok.AllArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class StartupInjectionService {

    private final ProfileServiceClient profileServiceClient;
    private final EmbeddingModel embeddingModel;
    private final ResumeChunkRepository resumeChunkRepository;

    private List<ResumeChunk> chunkProfile(ProfileResponse profile) {
        List<ResumeChunk> chunkList = new ArrayList<>();
        chunkList.add(addProfileInfoChunk(profile));
        chunkList.addAll(addExperienceChunk(profile));
        chunkList.addAll(addProjectChunk(profile));
        chunkList.addAll(addSkillsChunk(profile));
        chunkList.addAll(addEducationChunk(profile));
        chunkList.addAll(addCertificationChunk(profile));
        return  chunkList;
    }

    private ResumeChunk addProfileInfoChunk(ProfileResponse profile) {
        String profileChunk = "Name: " + profile.getName() + "\nTitle: " + profile.getTitle() + "\nLocation: " +
                profile.getLocation() + "\nEmail" + profile.getEmail() + "\nPhone: " + profile.getPhone() +
                "\nGithub: " + profile.getGithub() + "\nLinkedin: " + profile.getLinkedin() + "\nSummary: " + profile.getSummary();
        return ResumeChunk.builder()
                .chunkText(profileChunk).sectionType(ResumeSection.PROFILE_INFO.name()).sectionId(profile.getMongoId())
                .embedding(new PGvector(embeddingModel.embed(profileChunk)))
                .build();
    }

    private List<ResumeChunk> addExperienceChunk(ProfileResponse profile) {
        List<ResumeChunk> chunkList = new ArrayList<>();
        for (ExperienceDto exp : profile.getExperience()) {
            String chunkText = String.format("""
                Company: %s
                Role: %s
                From: %s To: %s
                Bullets: %s
                Tech: %s
                """,
                    exp.getCompany(),
                    exp.getRole(),
                    exp.getFrom(),
                    exp.getTo(),
                    String.join(", ", exp.getBullets()),
                    String.join(", ", exp.getTech())
            );
            ResumeChunk chunk = ResumeChunk.builder()
                    .chunkText(chunkText).sectionType(ResumeSection.EXPERIENCE.name()).sectionId(exp.getMongoId())
                    .embedding(new PGvector(embeddingModel.embed(chunkText)))
                    .build();
            chunkList.add(chunk);
        }
        return chunkList;
    }

    private List<ResumeChunk> addProjectChunk(ProfileResponse profile) {
        List<ResumeChunk> chunkList = new ArrayList<>();
        for (ProjectDto project : profile.getProjects()) {
            String chunkText = String.format("""
        Project: %s
        Description: %s
        Tech: %s
        """,
                    project.getTitle(),
                    String.join(", ", project.getDescription()),
                    String.join(", ", project.getTech())
            );
            ResumeChunk chunk = ResumeChunk.builder()
                    .chunkText(chunkText).sectionType(ResumeSection.PROJECTS.name()).sectionId(project.getMongoId())
                    .embedding(new PGvector(embeddingModel.embed(chunkText)))
                    .build();
            chunkList.add(chunk);
        }
        return chunkList;
    }

    private List<ResumeChunk> addSkillsChunk(ProfileResponse profile) {
        List<ResumeChunk> chunkList = new ArrayList<>();
        for (SkillsDto skill : profile.getSkills()) {

            String chunkText = String.format("""
                Category: %s
                Skills: %s
                """,
                    skill.getCategory(),
                    String.join(", ", skill.getItems())
            );

            ResumeChunk chunk = ResumeChunk.builder()
                    .chunkText(chunkText)
                    .sectionType(ResumeSection.SKILLS.name())
                    .sectionId(skill.getMongoId())
                    .embedding(new PGvector(embeddingModel.embed(chunkText)))
                    .build();

            chunkList.add(chunk);
        }
        return chunkList;
    }

    private List<ResumeChunk> addEducationChunk(ProfileResponse profile) {
        List<ResumeChunk> chunkList = new ArrayList<>();

        for (EducationDto edu : profile.getEducation()) {

            String chunkText = String.format("""
                Degree: %s
                University: %s
                """,
                    edu.getDegree(),
                    edu.getUniversity()
            );

            ResumeChunk chunk = ResumeChunk.builder()
                    .chunkText(chunkText)
                    .sectionType(ResumeSection.EDUCATION.name())
                    .sectionId(edu.getMongoId())
                    .embedding(new PGvector(embeddingModel.embed(chunkText)))
                    .build();

            chunkList.add(chunk);
        }

        return chunkList;
    }

    private List<ResumeChunk> addCertificationChunk(ProfileResponse profile) {
        List<ResumeChunk> chunkList = new ArrayList<>();

        for (CertificationDto cert : profile.getCertification()) {

            String chunkText = String.format("""
                Certification: %s
                Issuer: %s
                """,
                    cert.getName(),
                    cert.getIssuer()
            );

            ResumeChunk chunk = ResumeChunk.builder()
                    .chunkText(chunkText)
                    .sectionType(ResumeSection.CERTIFICATION.name())
                    .sectionId(cert.getMongoId())
                    .embedding(new PGvector(embeddingModel.embed(chunkText)))
                    .build();

            chunkList.add(chunk);
        }

        return chunkList;
    }


    @Transactional
    public void ingest() {
        if(resumeChunkRepository.count() == 0) {
            ProfileResponse profile = profileServiceClient.getResume();
            List<ResumeChunk> chunkList = chunkProfile(profile);
            chunkList.forEach(chunk -> {
                resumeChunkRepository.insertChunk(chunk.getChunkText(),chunk.getSectionId(),chunk.getSectionType(),chunk.getEmbedding().toString());
            });
        }
    }
}
