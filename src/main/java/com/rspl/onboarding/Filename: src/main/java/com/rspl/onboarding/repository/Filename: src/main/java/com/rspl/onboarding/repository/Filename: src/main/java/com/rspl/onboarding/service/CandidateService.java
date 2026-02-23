package com.rspl.onboarding.service;

import com.rspl.onboarding.dto.*;
import com.rspl.onboarding.entity.*;
import com.rspl.onboarding.enums.*;
import com.rspl.onboarding.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service @RequiredArgsConstructor @Slf4j @Transactional
public class CandidateService {
    private final CandidateRepository candidateRepo;
    private final HRExecutiveRepository hrRepo;
    private final AuditTrailRepository auditRepo;
    private final EmailService emailService;

    public Candidate initiate(CandidateRequest req) {
        if (candidateRepo.existsByEmailId(req.getEmailId()))
            throw new RuntimeException("Email already exists: " + req.getEmailId());
        if (candidateRepo.existsByAadhaarNo(req.getAadhaarNo()))
            throw new RuntimeException("Aadhaar already exists");
        HRExecutive hr = req.getAssignedHRId() != null
            ? hrRepo.findById(req.getAssignedHRId()).orElse(null) : null;
        Candidate c = Candidate.builder()
            .employeeName(req.getEmployeeName())
            .aadhaarNo(req.getAadhaarNo())
            .emailId(req.getEmailId())
            .mobileNo(req.getMobileNo())
            .designation(req.getDesignation())
            .joiningStatus(JoiningStatus.INITIATED)
            .linkStatus(LinkStatus.NOT_SENT)
            .onboardingToken(UUID.randomUUID().toString())
            .initiatedBy(req.getInitiatedBy())
            .assignedHR(hr)
            .build();
        candidateRepo.save(c);
        addAudit(c, "INITIATED", req.getInitiatedBy() != null ? req.getInitiatedBy() : "hr_admin", null);
        if (req.isSendLinkImmediately()) sendOnboardingLink(c.getId(), "System");
        return c;
    }

    public Page<Candidate> getAll(String search, String status, String linkStatus, Long hrId, int page, int size) {
        JoiningStatus js = (status != null && !status.isBlank()) ? JoiningStatus.valueOf(status) : null;
        LinkStatus ls = (linkStatus != null && !linkStatus.isBlank()) ? LinkStatus.valueOf(linkStatus) : null;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return candidateRepo.searchCandidates(
            search != null && !search.isBlank() ? search : null, js, ls, hrId, pageable);
    }

    public Candidate getById(Long id) {
        return candidateRepo.findById(id).orElseThrow(() -> new RuntimeException("Candidate not found: " + id));
    }

    public Candidate sendOnboardingLink(Long id, String sentBy) {
        Candidate c = getById(id);
        c.setLinkStatus(LinkStatus.SENT);
        c.setLinkSentAt(LocalDateTime.now());
        candidateRepo.save(c);
        addAudit(c, "LINK_SENT", sentBy, null);
        emailService.sendOnboardingLink(c.getEmailId(), c.getEmployeeName(), c.getOnboardingToken(), c.getDesignation());
        return c;
    }

    public Candidate markLinkOpened(String token) {
        Candidate c = candidateRepo.findByOnboardingToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (c.getLinkStatus() != LinkStatus.OPENED) {
            c.setLinkStatus(LinkStatus.OPENED);
            c.setLinkOpenedAt(LocalDateTime.now());
            candidateRepo.save(c);
            addAudit(c, "LINK_OPENED", "CANDIDATE", null);
        }
        return c;
    }

    public Candidate approve(Long id, String approvedBy) {
        Candidate c = getById(id);
        c.setJoiningStatus(JoiningStatus.APPROVED);
        c.setApprovedBy(approvedBy);
        c.setApprovedAt(LocalDateTime.now());
        candidateRepo.save(c);
        addAudit(c, "APPROVED", approvedBy, null);
        emailService.sendApprovalEmail(c.getEmailId(), c.getEmployeeName());
        return c;
    }

    public Candidate reject(Long id, RejectRequest req) {
        Candidate c = getById(id);
        c.setJoiningStatus(JoiningStatus.REJECTED);
        c.setRejectedBy(req.getRejectedBy());
        c.setRejectionReason(req.getReason());
        c.setRejectedAt(LocalDateTime.now());
        candidateRepo.save(c);
        addAudit(c, "REJECTED", req.getRejectedBy(), req.getReason());
        emailService.sendRejectionEmail(c.getEmailId(), c.getEmployeeName(), req.getReason());
        return c;
    }

    public Candidate updateForm(String token, Candidate formData) {
        Candidate c = candidateRepo.findByOnboardingToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));
        c.setFathersName(formData.getFathersName());
        c.setDob(formData.getDob());
        c.setGender(formData.getGender());
        c.setPanCardNo(formData.getPanCardNo());
        c.setBankName(formData.getBankName());
        c.setJoiningStatus(JoiningStatus.FORM_SUBMITTED);
        candidateRepo.save(c);
        addAudit(c, "FORM_SUBMITTED", "CANDIDATE", null);
        return c;
    }

    public Candidate markSigned(Long id) {
        Candidate c = getById(id);
        c.setJoiningStatus(JoiningStatus.SIGNED);
        candidateRepo.save(c);
        addAudit(c, "SIGNED", "CANDIDATE", null);
        return c;
    }

    private void addAudit(Candidate c, String action, String by, String remarks) {
        auditRepo.save(AuditTrail.builder()
            .candidate(c).action(action).performedBy(by)
            .remarks(remarks).performedAt(LocalDateTime.now()).build());
    }
}
