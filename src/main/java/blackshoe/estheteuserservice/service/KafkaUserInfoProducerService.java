package blackshoe.estheteuserservice.service;

import blackshoe.estheteuserservice.dto.UserDto;

public interface KafkaUserInfoProducerService {

    void createUser(UserDto.UserInfoDto userInfoDto);

}
