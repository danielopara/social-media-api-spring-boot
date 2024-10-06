package com.danielopara.Social_Media_API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    @NotBlank(message = "first name should not be blank")
    private String firstName;

    @NotBlank(message = "last name should not be blank")
    private String lastName;

    private List<String> otherNames;

    @NotBlank(message = "email should not be blank")
    @Email(message = "enter a valid email")
    private String email;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotBlank(message = "enter a password")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
