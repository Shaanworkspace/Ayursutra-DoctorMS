package com.doctorms.Messaging;


import com.doctorms.DTO.Response.MedicalRecord;
import com.doctorms.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class medicalRecordMessageConsumer {
    private final DoctorService doctorService;


    @RabbitListener(queues = "medicalRecordQueue")
    public void consumeMessage(MedicalRecord dto){

        log.info("Received message from medicalRecordQueue: {}", dto);

        try {
            log.info("Successfully added record {} to Doctor {}",
                    dto.getMedicalRecordId(), dto.getDoctorId());
        } catch (Exception ex) {
            log.error("!!!! Error processing message for Doctor {}: {}",
                    dto.getDoctorId(), ex.getMessage(), ex);
        }
    }

}
