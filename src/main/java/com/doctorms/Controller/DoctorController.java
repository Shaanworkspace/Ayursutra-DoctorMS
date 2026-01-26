package com.doctorms.Controller;


import com.doctorms.DTO.Request.RegisterRequestDTO;
import com.doctorms.DTO.Response.DoctorResponseDTO;
import com.doctorms.DTO.Response.MedicalRecord;
import com.doctorms.Entity.Doctor;
import com.doctorms.Repository.DoctorRepository;
import com.doctorms.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

	private final DoctorService doctorService;
	private final DoctorRepository doctorRepository;


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
	@GetMapping("/profile/me")
	public DoctorResponseDTO getMyProfile(Authentication authentication) {
		String email = (String) authentication.getPrincipal();
		boolean exist = doctorRepository.existsDoctorByEmail(email);
		if (!exist){
			throw new RuntimeException(
					"Doc not Exist for email: " + email
			);
		}
		log.info("Doctor controller METHOD : GET , REQUEST : profile/me , principle of authentication with user id:{} ",email);
		Doctor doctor = doctorRepository.findByEmail(email);
		if (doctor == null) throw new RuntimeException("Doctor not found for email: " + email);
		return doctorService.toDoctorResponseDTO(doctor);
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
	public boolean checkDoctorByUserId(@PathVariable String id) {
		return doctorService.checkDoctorById(id);
	}

	@GetMapping("/appointments/{id}")
	public ResponseEntity<List<MedicalRecord>> getAppointmentByDoctorId(@PathVariable String id) {
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
	Delete Mapping
	 */
	@DeleteMapping("/{id}")
	public void deleteDoctor(@PathVariable String id) {
		doctorService.deleteDoctor(id);
	}
}

