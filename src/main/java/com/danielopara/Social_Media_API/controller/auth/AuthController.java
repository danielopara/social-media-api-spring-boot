package com.danielopara.Social_Media_API.controller.auth;

import com.danielopara.Social_Media_API.dto.LoginDto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.auth.implementation.AuthService;
import com.danielopara.Social_Media_API.service.user.implementation.UserServiceImplementation;
import com.danielopara.Social_Media_API.utils.ResponseHandlers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Management", description = "Managing Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ResponseHandlers responseHandlers;

    @Operation(description = "Generated a token after successful authentication", summary = "Authenticates users")
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        BaseResponse response = authService.accountLogin(loginDto);
        return responseHandlers.handleResponse(response);
    }
}
