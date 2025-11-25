package com.doctorms.Client;

import com.doctorms.DTO.Response.DoctorMedicalRecordsDTO;
import com.doctorms.ENUM.Status;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${medical.record.service.name}",url = "${medicalrecord-service.url}")
public interface MedicalRecordClient {
    @GetMapping("/api/medical-records/for-doctor/{id}")
    DoctorMedicalRecordsDTO getRecordById(@PathVariable("id") Long medicalRecordId);

    @PutMapping("/api/medical-records/status-change/{id}")
    DoctorMedicalRecordsDTO medicalRecordStatusChange(@PathVariable("id") Long id, @RequestParam String status,@RequestParam Long doctorId);
}
