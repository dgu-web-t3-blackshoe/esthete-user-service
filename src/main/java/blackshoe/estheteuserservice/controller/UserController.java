package blackshoe.estheteuserservice.controller;


import blackshoe.estheteuserservice.dto.UserDto;
import blackshoe.estheteuserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto.CreateTestUserResponse> createTestUser(@RequestBody UserDto.CreateTestUserRequest createTestUserRequest){
        final UserDto.CreateTestUserResponse createTestUserResponse = userService.createTestUser(createTestUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(createTestUserResponse);
    }
}
