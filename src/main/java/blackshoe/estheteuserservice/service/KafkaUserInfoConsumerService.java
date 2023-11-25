package blackshoe.estheteuserservice.service;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaUserInfoConsumerService{
    void deleteUser(String payload, Acknowledgment acknowledgment);
}
