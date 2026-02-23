package com.rspl.onboarding.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hr_executives")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HRExecutive {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true) private String email;
    private String phone;
    private String role;
    private String employeeCode;
    private boolean active = true;
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }
}
