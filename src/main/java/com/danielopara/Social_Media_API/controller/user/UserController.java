package com.danielopara.Social_Media_API.controller.user;

import com.danielopara.Social_Media_API.dto.CreateUserDto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.user.implementation.UserServiceImplementation;
import com.danielopara.Social_Media_API.utils.ResponseHandlers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "Managing user endpoints")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImplementation userService;
    private final ResponseHandlers responseHandlers;

    @Operation(summary = "Creates a user", description="Endpoints manages the creation of users")
    @PostMapping("/users/create")
    ResponseEntity<?> createUser(@RequestBody CreateUserDto createUserDto){
        BaseResponse response = userService.createUser(createUserDto);
        return responseHandlers.handleResponse(response);
    }

    @Operation(summary="Lists all users", description = "To lists all the users")
    @GetMapping("/users")
    ResponseEntity<?> allUser(){
        BaseResponse users = userService.getUsers();
        return responseHandlers.handleResponse(users);
    }
}
