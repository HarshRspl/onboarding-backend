package com.rspl.onboarding.repository;
import com.rspl.onboarding.entity.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {
    List<AuditTrail> findByCandidateIdOrderByPerformedAtAsc(Long candidateId);
}
