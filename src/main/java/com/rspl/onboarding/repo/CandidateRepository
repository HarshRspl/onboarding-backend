package com.rspl.onboarding.repo;

import com.rspl.onboarding.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
  Optional<Candidate> findByEmailIdIgnoreCase(String emailId);
}
