package com.ll.mbooks.domain.email.repository;

import com.ll.mbooks.domain.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
}
