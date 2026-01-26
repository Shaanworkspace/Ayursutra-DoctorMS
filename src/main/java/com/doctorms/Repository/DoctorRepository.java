package com.doctorms.Repository;


import com.doctorms.Entity.Doctor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    List<Doctor> findBySpecialization(String specialization);

    boolean existsDoctorByUserId(String userId);

    Doctor findByUserId(String userId);

    boolean existsDoctorByEmail(String email);

    Doctor findByEmail(String email);
}