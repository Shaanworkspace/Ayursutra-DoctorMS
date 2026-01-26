package com.doctorms.Service;


import com.doctorms.Client.PatientClient;
import com.doctorms.DTO.Request.RegisterRequestDTO;
import com.doctorms.DTO.Response.DoctorResponseDTO;
import com.doctorms.DTO.Response.MedicalRecord;
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
        log.info("Fetching medical Record from patient");
        List<MedicalRecord> medicalRecordResponseDTOList = patientClient.medicalRecordsByDoctorId(doctor.getUserId());
        log.info("Got Medical Record : {}",medicalRecordResponseDTOList);
        return DoctorResponseDTO.builder()
                .email(doctor.getEmail())
                .userId(doctor.getUserId())
                .name(doctor.getDoctorName())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization())
                .hospitalAffiliation(doctor.getHospitalAffiliation())
                .medicalRecords(medicalRecordResponseDTOList)
                .build();
    }

}
