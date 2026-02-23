package com.rspl.onboarding.repository;
import com.rspl.onboarding.entity.HRExecutive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface HRExecutiveRepository extends JpaRepository<HRExecutive, Long> {
    List<HRExecutive> findByActiveTrue();
}
