package com.doctorms.Client.FallBack;

import com.doctorms.Client.PatientClient;
import com.doctorms.DTO.Response.MedicalRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PatientClientFallback implements PatientClient {
	@Override
	public List<MedicalRecord> medicalRecordsByDoctorId(String doctorId) {
		log.warn("Fall back NO medical records are found in patient By doctor id : {}",doctorId);
		return List.of();
	}
}
