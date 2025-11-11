package com.doctorms.Controller;


import com.doctorms.Client.MedicalRecordClient;
import com.doctorms.DTO.Response.DoctorMedicalRecordsDTO;
import com.doctorms.DTO.Response.DoctorResponseDTO;
import com.doctorms.Entity.Doctor;
import com.doctorms.Service.DoctorService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final MedicalRecordClient medicalRecordClient;


    /*
    Get Methods
     */

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        DoctorResponseDTO doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Doctor not found!");
        }
        return ResponseEntity.ok(doctor);
    }

    /*
    Post Methods
     */

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        try {
            Doctor doctor1 = doctorService.createDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(doctor1);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }



    /*
    Put Method
     */

    @PutMapping("/medical-record/{recordId}/status")
    @CircuitBreaker(name = "medicalRecordBreaker", fallbackMethod = "statusChangeFallback")
    @Retry(name = "medicalRecordRetry", fallbackMethod = "statusChangeFallback")
    @RateLimiter(name = "medicalRecordRateLimiter", fallbackMethod = "statusChangeFallback")
    public ResponseEntity<DoctorMedicalRecordsDTO> statusChange(@PathVariable Long recordId,
                                                                @RequestParam String status,@RequestParam Long doctorId){
        try{
            DoctorMedicalRecordsDTO updatedRecord = medicalRecordClient.medicalRecordStatusChange(recordId,status,doctorId);
            return ResponseEntity.ok(updatedRecord);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public String statusChangeFallback(Exception exception){
        return exception.toString();
    }


    /*
    Delete Mapping
     */
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}

