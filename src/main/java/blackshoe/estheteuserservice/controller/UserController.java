package blackshoe.estheteuserservice.controller;


import blackshoe.estheteuserservice.dto.UserDto;
import blackshoe.estheteuserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<UserDto.CreateTestUserResponse> createTestUser(UserDto.CreateTestUserRequest createTestUserRequest){
        final UserDto.CreateTestUserResponse createTestUserResponse = userService.createTestUser(createTestUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(createTestUserResponse);
    }
}
