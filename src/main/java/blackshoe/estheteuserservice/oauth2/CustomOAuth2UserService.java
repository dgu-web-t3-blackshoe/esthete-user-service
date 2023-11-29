package blackshoe.estheteuserservice.oauth2;


import blackshoe.estheteuserservice.dto.UserDto;
import blackshoe.estheteuserservice.entity.User;
import blackshoe.estheteuserservice.exception.UserErrorResult;
import blackshoe.estheteuserservice.exception.UserException;
import blackshoe.estheteuserservice.repository.UserRepository;
import blackshoe.estheteuserservice.service.KafkaUserInfoProducerService;
import blackshoe.estheteuserservice.vo.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service @Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaUserInfoProducerService kafkaUserInfoProducerService;

    public CustomOAuth2UserService() {
        super();
    }

    @Override
    @Transactional(noRollbackFor = UserException.class)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            log.error("Error while parsing OAuth2User attributes: {}", e.getMessage());
        }
        final String authProvider = userRequest.getClientRegistration().getClientName();
        final String email;

        if (authProvider.toLowerCase().equals("instagram")) {
            log.info("Auth provider: {}", authProvider);

            email = (String) oAuth2User.getAttributes().get("email");
        } else {
            email = null;
        }

        User user = null;

        log.info("Trying to pull user info email {} authProvider {} ", email, authProvider);

        try {
            if (userRepository.existsByEmail(email)) {
                user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

                user.setProvider(authProvider);
            } else {
                user = User.builder()
                        .email(email)
                        .role(Role.USER)
                        .provider(authProvider)
                        .build();

                userRepository.save(user);
                kafkaUserInfoProducerService.createUser(UserDto.UserInfoDto.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .build());
            }

        } catch (UsernameNotFoundException e) {
            log.error("User not found with email: {}", email);
            throw e;
        }

        if (user == null) {
            log.info("User is null");
            throw new UsernameNotFoundException("User 정보 초기화 안됨: " + email);
        }

        log.info("user email {}", user.getEmail());
        log.info("user authProvider {}", user.getProvider());

        return new CustomOAuth2User(user.getUserId().toString(), user.getEmail(), oAuth2User.getAttributes());
    }
}
