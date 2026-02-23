package com.rspl.onboarding.controller;

import com.rspl.onboarding.dto.ApiResponse;
import com.rspl.onboarding.entity.HRExecutive;
import com.rspl.onboarding.repository.HRExecutiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hr/team")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HRController {
    private final HRExecutiveRepository hrRepo;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HRExecutive>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(hrRepo.findByActiveTrue()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HRExecutive>> create(@RequestBody HRExecutive hr) {
        return ResponseEntity.ok(ApiResponse.ok("HR added", hrRepo.save(hr)));
    }
}
