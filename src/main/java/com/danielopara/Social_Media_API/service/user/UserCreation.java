package com.danielopara.Social_Media_API.service.user;

import com.danielopara.Social_Media_API.dto.CreateUserDto;
import com.danielopara.Social_Media_API.models.UserModel;
import com.danielopara.Social_Media_API.response.BaseResponse;

//user creation
public interface UserCreation {
    BaseResponse createUser(CreateUserDto createUserDto);
}
