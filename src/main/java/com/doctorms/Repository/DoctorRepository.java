package com.doctorms.Repository;


import com.doctorms.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Find doctor by specialization
    List<Doctor> findBySpecialization(String specialization);

    Optional<Doctor> findByEmail(String email);

}