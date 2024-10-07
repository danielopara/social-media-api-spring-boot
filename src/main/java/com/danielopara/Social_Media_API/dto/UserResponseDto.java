package com.danielopara.Social_Media_API.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private List<String> otherNames;
    private String email;
    private String phoneNumber;
}
