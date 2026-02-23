package com.rspl.onboarding.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trail")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditTrail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
    private String action;
    private String performedBy;
    private String remarks;
    private LocalDateTime performedAt;
    @PrePersist
    public void prePersist() { performedAt = LocalDateTime.now(); }
}
