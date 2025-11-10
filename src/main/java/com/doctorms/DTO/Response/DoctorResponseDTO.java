package com.doctorms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String hospitalAffiliation;

    List<DoctorMedicalRecordsDTO> medicalRecords;
}