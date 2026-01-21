package com.doctorms.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Doctor {

    @Id
    @Column(nullable = false)
    private String userId;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)   // Mobile number
    private String phoneNumber;

    @Column(length = 100)   // E.g. "Cardiologist", "Dentist"
    private String specialization;


    @Column(length = 50)
    private String qualification;  // e.g., "MBBS, MD"

    @Column(length = 200)
    private String hospitalAffiliation; // Hospital/clinic name

//    Relations
    private List<Long> medicalRecordIds = new ArrayList<>();

    private List<Long> therapyPlanIds = new ArrayList<>();
}