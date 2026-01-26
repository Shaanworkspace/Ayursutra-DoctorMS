package com.doctorms.Service;


import com.doctorms.Client.PatientClient;
import com.doctorms.DTO.Request.MedicalRecordRequestDTO;
import com.doctorms.DTO.Request.RegisterRequestDTO;
import com.doctorms.DTO.Response.DoctorResponseDTO;
import com.doctorms.DTO.Response.MedicalRecordResponseDTO;
import com.doctorms.Entity.Doctor;
import com.doctorms.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PatientClient patientClient;
    public Doctor createDoctor(RegisterRequestDTO doctor) {
        // Check if email already exists
        boolean exists = doctorRepository.existsDoctorByUserId(doctor.getUserId());
        if(exists){
            return doctorRepository.findByUserId(doctor.getUserId());
        }
        Doctor doctor1 = Doctor.builder()
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


    public DoctorResponseDTO toDoctorResponseDTO(Doctor doctor) {
        List<MedicalRecordResponseDTO>  medicalRecordResponseDTOList = patientClient.medicalRecordsByDoctorId(doctor.getUserId());
        return DoctorResponseDTO.builder()
                .userId(doctor.getUserId())
                .name(doctor.getDoctorName())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization())
                .hospitalAffiliation(doctor.getHospitalAffiliation())
                .medicalRecords(medicalRecordResponseDTOList)
                .build();
    }

	public Doctor findDoctorByUserId(String doctorId) {
        return doctorRepository.findByUserId(doctorId);
	}
}
