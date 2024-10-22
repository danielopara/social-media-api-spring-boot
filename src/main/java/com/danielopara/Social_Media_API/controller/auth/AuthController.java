package com.danielopara.Social_Media_API.controller.auth;

import com.danielopara.Social_Media_API.dto.LoginDto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.auth.implementation.AuthService;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        BaseResponse response = authService.accountLogin(loginDto);
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
