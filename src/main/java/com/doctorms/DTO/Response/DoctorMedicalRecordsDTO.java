package com.doctorms.DTO.Response;

import com.doctorms.ENUM.Status;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorMedicalRecordsDTO {

    private Long id;     // Medical record ID
    private Long patientId;       // Patient's full name
    private Status status;       // e.g. "PENDING", "ACTIVE", "COMPLETED"
}
