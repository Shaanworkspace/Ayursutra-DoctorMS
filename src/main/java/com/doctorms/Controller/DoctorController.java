package com.doctorms.Controller;


import com.doctorms.DTO.Request.MedicalRecordRequestDTO;
import com.doctorms.DTO.Request.RegisterRequestDTO;
import com.doctorms.DTO.Response.DoctorResponseDTO;
import com.doctorms.DTO.Response.MedicalRecordResponseDTO;
import com.doctorms.Entity.Doctor;
import com.doctorms.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

	private final DoctorService doctorService;


	/*
	Get Methods
	 */
	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("DOCTOR SERVICE UP");
	}

	@GetMapping
	public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
		return ResponseEntity.ok(doctorService.getAllDoctors());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getDoctorById(@PathVariable String id) {
		DoctorResponseDTO doctor = doctorService.getDoctorById(id);
		if (doctor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Doctor not found!");
		}
		return ResponseEntity.ok(doctor);
	}

	@GetMapping("/check/{id}")
	public Boolean checkDoctorByUserId(@PathVariable String id) {
		return doctorService.checkDoctorById(id);
	}

	@GetMapping("/appointments/{id}")
	public ResponseEntity<List<MedicalRecordResponseDTO>> getAppointmentByDoctorId(@PathVariable String id) {
		DoctorResponseDTO doctor = doctorService.getDoctorById(id);
		if (doctor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(doctor.getMedicalRecords());
	}


    /*
    Post Methods
     */

	@PostMapping
	public ResponseEntity<DoctorResponseDTO> addDoctor(@RequestBody RegisterRequestDTO doctor) {
		Doctor doctor1 = doctorService.createDoctor(doctor);
		return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.toDoctorResponseDTO(doctor1));
	}



    /*
    Put Method
     */


	@PutMapping("/medical-record/add")
	public void addMedicalRecord(@RequestBody MedicalRecordRequestDTO medicalRecordRequestDTO){
		Doctor doctor = doctorService.findDoctorByUserId(medicalRecordRequestDTO.getDoctorId());
		if(doctor==null) {
			log.error("Doctor not found with id :{}",medicalRecordRequestDTO.getDoctorId());
			return;
		}
		doctor.getMedicalRecordIds().add(medicalRecordRequestDTO.getMedicalRecordId());
		log.info("Added SuccessFully");
	}

	/*
	Delete Mapping
	 */
	@DeleteMapping("/{id}")
	public void deleteDoctor(@PathVariable String id) {
		doctorService.deleteDoctor(id);
	}
}

