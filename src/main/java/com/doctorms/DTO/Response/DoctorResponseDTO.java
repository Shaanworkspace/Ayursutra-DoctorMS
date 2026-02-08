package com.doctorms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {
    private String email;
    private String userId;
    private String phoneNumber;
    private String name;
    private String specialization;
    private String availability;
    private String hospitalAffiliation;
    @Builder.Default
    private String role="DOCTOR";
}