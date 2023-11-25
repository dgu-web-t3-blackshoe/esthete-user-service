package blackshoe.estheteuserservice.service;
import blackshoe.estheteuserservice.dto.UserDto;

public interface UserService {

    UserDto.CreateTestUserResponse createTestUser(UserDto.CreateTestUserRequest createTestUserRequest);
}
