package com.doctorms.Service;


import com.doctorms.DTO.Request.MedicalRecordRequestDTO;
import com.doctorms.DTO.Request.RegisterRequestDTO;
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
    private final DoctorRepository doctorRepository;
    public Doctor createDoctor(RegisterRequestDTO doctor) {
        // Check if email already exists
        if (doctorRepository.findByUserId(doctor.getUserId()).isPresent()) {
            throw new IllegalArgumentException("Patient with this id already exists");
        }

        Doctor doctor1 = Doctor.builder()
                .userId(doctor.getUserId())
                .password(doctor.getPassword())
                .build();

        return doctorRepository.save(doctor1);
    }

    public DoctorResponseDTO getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::mapDoctorToDto)
                .orElse(null);
    }


    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::mapDoctorToDto)    // entity -> DTO conversion
                .collect(Collectors.toList());
    }


    public DoctorResponseDTO mapDoctorToDto(Doctor doctor) {
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

        return new DoctorResponseDTO(
                doctor.getUserId(),
                doctor.getPhoneNumber(),
                doctor.getSpecialization(),
                doctor.getHospitalAffiliation(),
                recordResponses
        );
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

    public Boolean checkDoctorById(String id) {
        return doctorRepository.existsDoctorByUserId(id);
    }
}
