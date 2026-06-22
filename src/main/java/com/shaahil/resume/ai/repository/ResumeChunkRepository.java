package com.shaahil.resume.ai.repository;

import com.shaahil.resume.ai.entity.ResumeChunk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResumeChunkRepository extends JpaRepository<ResumeChunk, Long> {
    @Query(value = "SELECT chunk_text FROM resume_chunks ORDER BY embedding <-> CAST(:queryVector AS vector) LIMIT :limit", nativeQuery = true)
    List<String> findSimilarChunks(@Param("queryVector") String queryVector, @Param("limit") int limit);

    Optional<ResumeChunk> findBySectionIdAndSectionType(String sectionId, String sectionType);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
INSERT INTO resume_chunks
(chunk_text, section_id, section_type, embedding)
VALUES (
    :chunkText,
    :sectionId,
    :sectionType,
    CAST(:embedding AS vector)
)
""", nativeQuery = true)
    int insertChunk(
            @Param("chunkText") String chunkText,
            @Param("sectionId") String sectionId,
            @Param("sectionType") String sectionType,
            @Param("embedding") String embedding
    );

}