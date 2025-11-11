package com.doctorms.Service;


import com.doctorms.DTO.Request.MedicalRecordRequestDTO;
import com.doctorms.DTO.Response.DoctorMedicalRecordsDTO;
import com.doctorms.DTO.Response.DoctorResponseDTO;
import com.doctorms.Entity.Doctor;
import com.doctorms.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RestTemplate restTemplate;



    public Doctor addDoctor(Doctor doctor) {
        // Check if email already exists
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }
        // Let timestamps be handled by entity @PrePersist
        return doctorRepository.save(doctor);
    }

    public DoctorResponseDTO getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::mapDoctorToDto)
                .orElse(null);
    }

    // Get doctors by specialization
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public List<DoctorResponseDTO> getAllDoctors() {
        // 1. Doctor entity nikaalo DB se
        // 2. Har Doctor ko mapDoctorToDto() se DTO banake collect karo
        return doctorRepository.findAll()
                .stream()
                .map(this::mapDoctorToDto)    // entity -> DTO conversion
                .collect(Collectors.toList());
    }
    // Convert Doctor entity -> DoctorResponseDTO
    public DoctorResponseDTO mapDoctorToDto(Doctor doctor) {

        // Agar doctor ke medicalRecords hain to unhe DTO me convert karo
        List<DoctorMedicalRecordsDTO> recordResponses =  new ArrayList<>();
        if (doctor.getMedicalRecordIds() != null && !doctor.getMedicalRecordIds().isEmpty()) {
            for (Long recordId : doctor.getMedicalRecordIds()) {
                DoctorMedicalRecordsDTO recordDTO = DoctorMedicalRecordsDTO.builder()
                        .id(recordId)
                        .patientId(null)
                        .status(null)
                        .build();

                recordResponses.add(recordDTO);
            }
        }


        // Ab doctor ka DTO banaao
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName() +" "+doctor.getLastName(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getSpecialization(),
                doctor.getHospitalAffiliation(),
                recordResponses
        );
    }


    public Doctor login(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("!! Doctor not found with email: " + email));

        if (!doctor.getPassword().equals(password)) {
            throw new IllegalArgumentException("!! Invalid password");
        }
        return doctor;
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> createDoctors(List<Doctor> doctors) {
        return doctorRepository.saveAll(doctors);
    }


    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public void addMedicalRecordToDoctor(MedicalRecordRequestDTO medicalRecordRequestDTO) {
        try {
            Doctor doctor = doctorRepository.findById(medicalRecordRequestDTO.getDoctorId()).orElseThrow(()->new RuntimeException("Doctor not found with id: "+ medicalRecordRequestDTO.getDoctorId()));
            doctor.getMedicalRecordIds().add(medicalRecordRequestDTO.getMedicalRecordId());
            doctorRepository.save(doctor);
        } catch (RuntimeException e){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
