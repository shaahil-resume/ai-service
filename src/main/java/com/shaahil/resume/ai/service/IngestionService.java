package com.shaahil.resume.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgvector.PGvector;
import com.shaahil.resume.ai.dto.*;
import com.shaahil.resume.ai.entity.ResumeChunk;
import com.shaahil.resume.ai.repository.ResumeChunkRepository;
import com.shaahil.resume.ai.util.OperationType;
import com.shaahil.resume.ai.util.ResumeSection;
import lombok.AllArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IngestionService {

    private final ObjectMapper objectMapper;
    private final EmbeddingModel embeddingModel;
    private final ResumeChunkRepository resumeChunkRepository;



    public void handleSectionUpdate(SectionUpdateRequest sectionUpdateRequest) {
        sectionUpdateRequest.getContent().forEach(section -> {
            ResumeChunk chunk = castClassToContents(sectionUpdateRequest.getSectionType(),section);
            Optional<ResumeChunk> existingChunk =  resumeChunkRepository.findBySectionIdAndSectionType(chunk.getSectionId(), chunk.getSectionType());
            switch(section.getOperation()) {
                case OperationType.INSERT:
                    if(existingChunk.isEmpty()) {
                        resumeChunkRepository.save(chunk);
                    }
                    break;
                case OperationType.UPDATE:
                    if(existingChunk.isPresent()) {
                        ResumeChunk existing = existingChunk.get();
                        existing.setChunkText(chunk.getChunkText());
                        existing.setEmbedding(chunk.getEmbedding());
                        resumeChunkRepository.save(existing);
                    }
                    break;
                case OperationType.DELETE:
                    if(existingChunk.isPresent()) {
                        resumeChunkRepository.delete(chunk);
                    }
                    break;
            }
        });

    }

    private ResumeChunk addProfileInfoChunk(ProfileResponse profile) {
        String profileChunk = "Name: " + profile.getName() + "Title: " + profile.getTitle() + "Location: " +
                profile.getLocation() + "Email" + profile.getEmail() + "Phone: " + profile.getPhone() +
                "Github: " + profile.getGithub() + "Linkedin: " + profile.getLinkedin() + "Summary: " + profile.getSummary();
        return ResumeChunk.builder()
                .chunkText(profileChunk)
                .embedding(new PGvector(embeddingModel.embed(profileChunk)))
                .build();
    }

    private ResumeChunk addExperienceChunk(ExperienceDto exp) {
        StringBuilder experienceChunks = new StringBuilder();
        experienceChunks.append("Company: ").append(exp.getCompany()).append("\nRole: ")
                .append(exp.getRole()).append("\nFrom: ").append(exp.getFrom()).append(" To: ")
                .append(exp.getTo()).append("\nBullets: ").append(String.join(", ", exp.getBullets()))
                .append("\nTech: ").append(String.join(", ", exp.getTech()));
        return ResumeChunk.builder()
                .chunkText(experienceChunks.toString())
                .embedding(new PGvector(embeddingModel.embed(experienceChunks.toString())))
                .build();
    }

    private ResumeChunk addEducationChunk(EducationDto education) {
        StringBuilder educationChunks = new StringBuilder();
        educationChunks.append(education.getDegree())
                .append(" at ")
                .append(education.getUniversity())
                .append("\n");
        return ResumeChunk.builder()
                .chunkText(educationChunks.toString())
                .embedding(new PGvector(embeddingModel.embed(educationChunks.toString())))
                .build();
    }

    private ResumeChunk addCertificationChunk(CertificationDto cert) {
        StringBuilder certChunk = new StringBuilder();
        certChunk.append(cert.getName())
                .append(" by ")
                .append(cert.getIssuer())
                .append("\n");
        return ResumeChunk.builder()
                .chunkText(certChunk.toString())
                .embedding(new PGvector(embeddingModel.embed(certChunk.toString())))
                .build();
    }

    private ResumeChunk addSkillsSection(SkillsDto skill) {
        StringBuilder skillsChunk = new StringBuilder();
        skillsChunk.append(skill.getCategory())
                .append(": ")
                .append(String.join(", ", skill.getItems()))
                .append("\n");
        return ResumeChunk.builder()
                .chunkText(skillsChunk.toString())
                .embedding(new PGvector(embeddingModel.embed(skillsChunk.toString())))
                .build();
    }

    private ResumeChunk addProjectSection(ProjectDto project) {
        StringBuilder projectChunk = new StringBuilder();
        projectChunk.append("Project: ").append(project.getTitle())
                .append("\nDescription: ").append(String.join(", ", project.getDescription()))
                .append("\nTech: ").append(String.join(", ", project.getTech()));
        return ResumeChunk.builder()
                .chunkText(projectChunk.toString())
                .embedding(new PGvector(embeddingModel.embed(projectChunk.toString())))
                .build();
    }



    private ResumeChunk castClassToContents(ResumeSection type,SectionUpdateItem sectionUpdateItem) {
        ResumeChunk chunk = switch (type) {
            case PROFILE_INFO -> {
                ProfileResponse response = objectMapper.convertValue(sectionUpdateItem.getItems(), ProfileResponse.class);
                yield addProfileInfoChunk(response);
            }
            case EXPERIENCE -> {
                ExperienceDto experience = objectMapper.convertValue(sectionUpdateItem.getItems(), ExperienceDto.class);
                yield addExperienceChunk(experience);
            }
            case EDUCATION -> {
                EducationDto education = objectMapper.convertValue(sectionUpdateItem.getItems(), EducationDto.class);
                yield addEducationChunk(education);
            }
            case CERTIFICATION -> {
                CertificationDto certification = objectMapper.convertValue(sectionUpdateItem.getItems(), CertificationDto.class);
                yield addCertificationChunk(certification);
            }
            case SKILLS -> {
                SkillsDto skills = objectMapper.convertValue(sectionUpdateItem.getItems(), SkillsDto.class);
                yield addSkillsSection(skills);
            }
            case PROJECTS -> {
                ProjectDto projects = objectMapper.convertValue(sectionUpdateItem.getItems(), ProjectDto.class);
                yield addProjectSection(projects);
            }
        };
        chunk.setSectionId(sectionUpdateItem.getSectionId());
        chunk.setSectionType(type.name());
        return chunk;

    }




}
