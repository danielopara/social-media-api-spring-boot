package com.danielopara.Social_Media_API.service.account.profilePhoto.profileInterface;

import com.danielopara.Social_Media_API.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePhotoInterface {
    BaseResponse uploadProfilePhoto(String email, MultipartFile file);
    BaseResponse getProfileImage(String email);
    BaseResponse updateProfilePhoto(String email, MultipartFile image);
    BaseResponse getProfilePhotoByUsername(String username);
}
