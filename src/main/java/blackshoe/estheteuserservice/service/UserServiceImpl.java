package blackshoe.estheteuserservice.service;

import blackshoe.estheteuserservice.dto.JwtDto;
import blackshoe.estheteuserservice.dto.UserDto;
import blackshoe.estheteuserservice.entity.User;
import blackshoe.estheteuserservice.repository.UserRepository;
import blackshoe.estheteuserservice.security.JwtTokenFilter;
import blackshoe.estheteuserservice.security.JwtTokenProvider;
import blackshoe.estheteuserservice.vo.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service @Slf4j @RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final KafkaUserInfoProducerService kafkaUserInfoProducerService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override @Transactional
    public UserDto.CreateTestUserResponse createTestUser(UserDto.CreateTestUserRequest createTestUserRequest) {

        User user = User.builder()
                .userId(UUID.randomUUID())
                .email(createTestUserRequest.getEmail())
                .provider(createTestUserRequest.getProvider())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        JwtDto.JwtRequestDto jwtRequestDto = JwtDto.JwtRequestDto.builder()
                .email(createTestUserRequest.getEmail())
                .userId(user.getUserId())
                .build();

        final String jwt = jwtTokenProvider.createAccessToken(jwtRequestDto);

        UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.builder()
                .email(createTestUserRequest.getEmail())
                .userId(user.getUserId())
                .role(Role.USER.getRoleName())
                .build();

        //kafkaUserInfoProducerService.createUser(userInfoDto);

        return UserDto.CreateTestUserResponse.builder()
                .userId(userInfoDto.getUserId())
                .jwt(jwt)
                .createdAt(String.valueOf(LocalDateTime.now()))
                .build();
    }
}

