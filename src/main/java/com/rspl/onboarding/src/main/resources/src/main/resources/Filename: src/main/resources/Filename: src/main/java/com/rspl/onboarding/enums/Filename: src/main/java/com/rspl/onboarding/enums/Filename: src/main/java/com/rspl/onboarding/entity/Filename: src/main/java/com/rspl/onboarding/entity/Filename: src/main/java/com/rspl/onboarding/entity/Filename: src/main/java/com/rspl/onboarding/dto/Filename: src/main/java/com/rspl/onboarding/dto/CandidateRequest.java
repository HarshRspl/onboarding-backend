package com.rspl.onboarding.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CandidateRequest {
    @NotBlank private String employeeName;
    @NotBlank @Pattern(regexp="^[2-9][0-9]{11}$") private String aadhaarNo;
    @NotBlank @Email private String emailId;
    @NotBlank @Pattern(regexp="^[6-9]\\d{9}$") private String mobileNo;
    @NotBlank private String designation;
    private Long assignedHRId;
    private String initiatedBy;
    private boolean sendLinkImmediately = true;
}
