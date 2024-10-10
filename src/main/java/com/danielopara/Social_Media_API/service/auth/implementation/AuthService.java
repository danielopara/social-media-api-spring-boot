package com.danielopara.Social_Media_API.service.auth.implementation;

import com.danielopara.Social_Media_API.Repository.UserRepository;
import com.danielopara.Social_Media_API.dto.LoginDto;
import com.danielopara.Social_Media_API.jwt.JwtService;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.auth.AuthInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthInterface {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public BaseResponse accountLogin(LoginDto login) {
        try{

            if (login == null) {
                return new BaseResponse(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "no content",
                        null
                );
            }

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getEmail(),
                            login.getPassword()
                    )
            );

            if(authenticate == null || !authenticate.isAuthenticated()){
                return new BaseResponse(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "username or password is incorrect",
                        null
                );
            }

            String token = jwtService.generateAccessToken(authenticate);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            return new BaseResponse(
                    HttpServletResponse.SC_OK,
                    "user logged in successfully",
                    response
            );

        } catch (Exception e){
            return new BaseResponse(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    e.getMessage()
            );
        }
    }
}
