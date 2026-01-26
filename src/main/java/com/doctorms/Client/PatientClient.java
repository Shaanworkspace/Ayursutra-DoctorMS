package com.doctorms.Client;
import com.doctorms.DTO.Response.MedicalRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
		name ="patient-service",
		url = "${services.patient.url}"
)
public interface PatientClient {
	@GetMapping("/api/patients/medical-records/doc/{doctorId}")
	List<MedicalRecord> medicalRecordsByDoctorId(@PathVariable String doctorId);
}
