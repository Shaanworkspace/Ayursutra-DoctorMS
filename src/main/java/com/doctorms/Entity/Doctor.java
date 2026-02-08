package com.doctorms.Entity;

import com.doctorms.ENUM.DoctorSpecialization;
import com.doctorms.ENUM.Availability;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 100)
    private DoctorSpecialization specialization;

    @Column(nullable = true, length = 50)
    private String qualification;

    @Column(nullable = true, length = 200)
    private String hospitalAffiliation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private Availability availability;

    @ElementCollection
    private List<String> therapyPlanIds = new ArrayList<>();
}
