package com.rspl.onboarding.entity;

import com.rspl.onboarding.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "candidates")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Candidate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeName;
    @Column(unique=true) private String aadhaarNo;
    @Column(unique=true) private String emailId;
    private String mobileNo;
    private String designation;
    private String fathersName;
    private String dob;
    private String gender;
    private String panCardNo;
    private String bankName;
    private String bankAccountNo;
    private String ifscCode;
    private String presentAddress;
    private String permanentAddress;
    @Enumerated(EnumType.STRING)
    private JoiningStatus joiningStatus = JoiningStatus.INITIATED;
    @Enumerated(EnumType.STRING)
    private LinkStatus linkStatus = LinkStatus.NOT_SENT;
    private String onboardingToken;
    private LocalDateTime linkSentAt;
    private LocalDateTime linkOpenedAt;
    private String initiatedBy;
    private String approvedBy;
    private String rejectedBy;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_hr_id")
    private HRExecutive assignedHR;
    @OneToMany(mappedBy="candidate", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @Builder.Default
    private List<AuditTrail> auditTrail = new ArrayList<>();
    @PrePersist
    public void prePersist() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
