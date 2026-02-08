package com.doctorms.Service;


import com.doctorms.Client.PatientClient;
import com.doctorms.DTO.Request.DoctorUpdateDTO;
import com.doctorms.DTO.Request.RegisterRequestDTO;
import com.doctorms.DTO.Response.DoctorResponseDTO;
import com.doctorms.DTO.Response.MedicalRecord;
import com.doctorms.ENUM.Availability;
import com.doctorms.ENUM.DoctorSpecialization;
import com.doctorms.Entity.Doctor;
import com.doctorms.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PatientClient patientClient;
    public Doctor createDoctor(RegisterRequestDTO doctor) {
        // Check if email already exists
        boolean exists = doctorRepository.existsDoctorByEmail(doctor.getEmail());
        if(exists){
            return doctorRepository.findByEmail(doctor.getEmail());
        }
        Doctor doctor1 = Doctor.builder()
                .email(doctor.getEmail())
                .userId(doctor.getUserId())
                .doctorName(doctor.getName())
                .password(doctor.getPassword())
                .build();

        return doctorRepository.save(doctor1);
    }

    public DoctorResponseDTO getDoctorById(String id) {
        return doctorRepository.findById(id)
                .map(this::toDoctorResponseDTO)
                .orElse(null);
    }

    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::toDoctorResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }


    public boolean checkDoctorById(String id) {
        return doctorRepository.existsDoctorByUserId(id);
    }

    public DoctorResponseDTO toDoctorResponseDTO(Doctor doctor) {
        log.info("Fetching medical records for doctor: {}", doctor.getUserId());

        return DoctorResponseDTO.builder()
                .email(doctor.getEmail())
                .userId(doctor.getUserId())
                .name(doctor.getDoctorName())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization() != null ? doctor.getSpecialization().name() : null)
                .availability(doctor.getAvailability() != null ? doctor.getAvailability().name() : null)
                .hospitalAffiliation(doctor.getHospitalAffiliation())
                .build();
    }

    public DoctorResponseDTO updateDoctorProfile(String email, DoctorUpdateDTO dto) {

        Doctor doctor = doctorRepository.findByEmail(email);

        if (doctor == null) {
            throw new RuntimeException("Doctor not found");
        }

        if (dto.getSpecialization() != null) {
            doctor.setSpecialization(
                    DoctorSpecialization.valueOf(dto.getSpecialization())
            );
        }

        if (dto.getAvailability() != null) {
            doctor.setAvailability(
                    Availability.valueOf(dto.getAvailability())
            );
        }

        if (dto.getHospitalAffiliation() != null) {
            doctor.setHospitalAffiliation(dto.getHospitalAffiliation());
        }

        if (dto.getPhoneNumber() != null) {
            doctor.setPhoneNumber(dto.getPhoneNumber());
        }

        Doctor saved = doctorRepository.save(doctor);

        return toDoctorResponseDTO(saved);
    }

}
