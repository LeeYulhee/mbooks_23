package com.ll.mbooks.domain.cash.repository;

import com.ll.mbooks.domain.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
