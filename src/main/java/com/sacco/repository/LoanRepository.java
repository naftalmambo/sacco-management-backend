package com.sacco.repository;

import com.sacco.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findAllByMemberId(Long memberId);
}
