package com.doctorms.DTO.Request;
import lombok.Data;

@Data
public class DoctorUpdateDTO {

	private String specialization;
	private String availability;
	private String hospitalAffiliation;
	private String phoneNumber;
}
