package com.rspl.onboarding.controller;

import com.rspl.onboarding.dto.*;
import com.rspl.onboarding.entity.Candidate;
import com.rspl.onboarding.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr/candidates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<Candidate>> initiate(@Valid @RequestBody CandidateRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Candidate initiated", candidateService.initiate(req)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Candidate>>> getAll(
        @RequestParam(required=false) String search,
        @RequestParam(required=false) String status,
        @RequestParam(required=false) String linkStatus,
        @RequestParam(required=false) Long hrId,
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(candidateService.getAll(search, status, linkStatus, hrId, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Candidate>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(candidateService.getById(id)));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Candidate>> approve(@PathVariable Long id, @RequestBody(required=false) RejectRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Approved", candidateService.approve(id, "hr_manager")));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Candidate>> reject(@PathVariable Long id, @RequestBody RejectRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Rejected", candidateService.reject(id, req)));
    }

    @PostMapping("/{id}/send-link")
    public ResponseEntity<ApiResponse<Candidate>> sendLink(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Link sent", candidateService.sendOnboardingLink(id, "hr_admin")));
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<ApiResponse<Candidate>> getByToken(@PathVariable String token) {
        return ResponseEntity.ok(ApiResponse.ok(candidateService.markLinkOpened(token)));
    }

    @PutMapping("/token/{token}/form")
    public ResponseEntity<ApiResponse<Candidate>> submitForm(@PathVariable String token, @RequestBody Candidate data) {
        return ResponseEntity.ok(ApiResponse.ok("Form submitted", candidateService.updateForm(token, data)));
    }

    @PostMapping("/{id}/sign")
    public ResponseEntity<ApiResponse<Candidate>> markSigned(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Signed", candidateService.markSigned(id)));
    }
}
