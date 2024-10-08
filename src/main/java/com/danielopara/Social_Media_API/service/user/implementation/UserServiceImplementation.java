package com.danielopara.Social_Media_API.service.user.implementation;

import com.danielopara.Social_Media_API.Repository.UserRepository;
import com.danielopara.Social_Media_API.dto.CreateUserDto;
import com.danielopara.Social_Media_API.dto.UserResponseDto;
import com.danielopara.Social_Media_API.models.UserModel;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${VALIDATION_EMAIL.regexp}")
    private String emailRegex;

    @Override
    public BaseResponse createUser(CreateUserDto createUserDto) {
        try{
            String validationError = validateUserInput(createUserDto);
            if (validationError != null) {
                return new BaseResponse(
                        HttpServletResponse.SC_BAD_REQUEST,
                        validationError,
                        null
                );
            }
            Pattern pattern = Pattern.compile(emailRegex);
            if(createUserDto.getEmail() == null || !pattern.matcher(createUserDto.getEmail()).matches()){
                return new BaseResponse(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "invalid email format",
                        null
                );
            }

            if(createUserDto.getPhoneNumber() == null || !createUserDto.getPhoneNumber().matches("\\d{11}")){
                return new BaseResponse(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "phone number must be 11 digits",
                        createUserDto.getPhoneNumber().length()
                );
            }

            Optional<UserModel> email = userRepository.findByEmail(createUserDto.getEmail());
            if(email.isPresent()){
                return new BaseResponse(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "email already exist",
                        null
                );
            }


            UserModel user = new UserModel();
            user.setFirstName(createUserDto.getFirstName());
            user.setLastName(createUserDto.getLastName());
            user.setEmail(createUserDto.getEmail().toLowerCase());
            user.setOtherNames(createUserDto.getOtherNames());
            user.setPhoneNumber(createUserDto.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

            userRepository.save(user);
            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("firstName", createUserDto.getFirstName());
            userResponse.put("lastName", createUserDto.getLastName());
            userResponse.put("email", createUserDto.getEmail().toLowerCase());

            return new BaseResponse(
                    HttpServletResponse.SC_CREATED,
                    "user created",
                    userResponse
            );

        } catch(Exception e){
            return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error", e.getMessage());
        }
    }


    private String validateUserInput(CreateUserDto createUserDto) {
        if (isEmpty(createUserDto.getFirstName()) || isEmpty(createUserDto.getLastName()) ||
                isEmpty(createUserDto.getEmail()) || isEmpty(createUserDto.getPassword()) ||
                isEmpty(createUserDto.getPhoneNumber())) {
            return "Error, field cannot be empty";
        }

        if (!createUserDto.getEmail().matches(emailRegex)) {
            return "Invalid email format";
        }

        if (!createUserDto.getPhoneNumber().matches("\\d{11}")) {
            return "Phone number must be 11 digits";
        }

        return null;
    }
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public BaseResponse getUsers() {
        try{
            List<UserModel> allUsers = userRepository.findAll();

            List<UserResponseDto> userResponse = allUsers.stream()
                    .map(user -> new UserResponseDto(
                            user.getFirstName(),
                            user.getLastName(),
                            user.getOtherNames(),
                            user.getEmail(),
                            user.getPhoneNumber()
                    ))
                    .toList();

            return new BaseResponse(
                    HttpServletResponse.SC_OK,
                    "all users",
                    userResponse
            );

        } catch (Exception e){
            return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error", e.getMessage());
        }
    }
}
