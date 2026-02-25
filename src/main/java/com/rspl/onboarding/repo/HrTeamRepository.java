package com.rspl.onboarding.repo;

import com.rspl.onboarding.domain.HrTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrTeamRepository extends JpaRepository<HrTeamMember, Integer> {
}
