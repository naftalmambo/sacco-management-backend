package com.sacco.repository;

import com.sacco.domain.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<LedgerTransaction, Long> {

    List<LedgerTransaction> findTop10ByAccountIdOrderByCreatedAtDesc(Long accountId);
}
