package blackshoe.estheteuserservice.service;

import blackshoe.estheteuserservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j @RequiredArgsConstructor @Service
public class KafkaUserInfoConsumerServiceImpl implements KafkaUserInfoConsumerService{

    private final UserRepository userRepository;

    @Override
    @KafkaListener(topics = "user-delete")
    @Transactional
    public void deleteUser(String payload, Acknowledgment acknowledgment) {
        log.info("received payload='{}'", payload);

        String userId = payload;

        userRepository.deleteByUserId(UUID.fromString(userId));

        acknowledgment.acknowledge();
    }

}
