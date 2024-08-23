package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.EnrollmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaNotificationsService {

    private final KafkaTemplate<String, EnrollmentDTO> kafkaTemplate;

    @Autowired
    public KafkaNotificationsService(KafkaTemplate<String, EnrollmentDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEnrollmentNotification(EnrollmentDTO enrollmentDTO) {
        CompletableFuture<SendResult<String, EnrollmentDTO>> future =
                kafkaTemplate.send("course_notifications", enrollmentDTO);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent to topic {}", result.getRecordMetadata().topic());
            } else {
                log.error("Unable to send message to topic {}", result.getRecordMetadata().topic(), ex);
            }
        });
    }
}
