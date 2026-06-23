package com.shaahil.resume.ai.repository;

import com.shaahil.resume.ai.entity.ChatLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogRepository extends JpaRepository<ChatLog,Long> {
    Page<ChatLog> findByEmail(String email, Pageable pageable);

}
