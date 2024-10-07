package com.danielopara.Social_Media_API.controller.user;

import com.danielopara.Social_Media_API.dto.CreateUserDto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.user.implementation.UserServiceImplementation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all-user")
    ResponseEntity<?> allUser(){
        BaseResponse users = userService.getUsers();
        if(users.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(users, HttpStatus.BAD_REQUEST);
        }
    }
}
