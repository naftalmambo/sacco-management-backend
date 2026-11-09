package com.sacco.repository;

import com.sacco.domain.SaccoMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<SaccoMember, Long> {

    Optional<SaccoMember> findByNationalId(String nationalId);
}
