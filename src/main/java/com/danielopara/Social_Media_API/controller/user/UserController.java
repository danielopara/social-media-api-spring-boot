package com.danielopara.Social_Media_API.controller.user;

import com.danielopara.Social_Media_API.dto.CreateUserDto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.user.implementation.UserServiceImplementation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImplementation userService;

    @PostMapping("/create-user")
    ResponseEntity<?> createUser(@RequestBody CreateUserDto createUserDto){
        BaseResponse response = userService.createUser(createUserDto);
        if(response.getStatusCode() == HttpServletResponse.SC_CREATED){
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
