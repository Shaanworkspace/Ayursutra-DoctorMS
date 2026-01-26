package com.doctorms.Entity;

import com.doctorms.DTO.Response.MedicalRecord;
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
    private String userId;

    private String doctorName;

    @Column(unique = true)
    private String email;
    @Column(nullable = true)
    private String password;

    @Column(nullable = true, length = 15)
    private String phoneNumber;

    @Column(nullable = true, length = 100)
    private String specialization;

    @Column(nullable = true, length = 50)
    private String qualification;

    @Column(nullable = true, length = 200)
    private String hospitalAffiliation;

    private List<String> therapyPlanIds = new ArrayList<>();
}