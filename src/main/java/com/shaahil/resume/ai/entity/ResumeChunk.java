package com.shaahil.resume.ai.entity;
import com.pgvector.PGvector;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@Table(name="resume_chunks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sectionType", "sectionId"}))
@NoArgsConstructor
@AllArgsConstructor
public class ResumeChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "vector(1536)")
    private PGvector embedding;

    @Column(columnDefinition = "TEXT")
    private String chunkText;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private String sectionId;

    @Column
    private String sectionType;
}
