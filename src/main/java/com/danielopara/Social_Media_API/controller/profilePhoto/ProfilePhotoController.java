package com.danielopara.Social_Media_API.controller.profilePhoto;

import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.profilePhoto.profileService.ProfilePhotoService;
import com.danielopara.Social_Media_API.utils.ResponseHandlers;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/social/profilePhoto")
@Tag(name = "Profile photo", description = "services for handling profile photo")
public class ProfilePhotoController {
    private final ProfilePhotoService profilePhotoService;
    private final ResponseHandlers responseHandlers;

    public ProfilePhotoController(ProfilePhotoService profilePhotoService, ResponseHandlers responseHandlers) {
        this.profilePhotoService = profilePhotoService;
        this.responseHandlers = responseHandlers;
    }


    @PostMapping(value = "/upload-profilePhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadProfilePhoto(@AuthenticationPrincipal UserDetails currentUser,
                                                           @RequestParam("file")MultipartFile file){
        String email = currentUser.getUsername();
        BaseResponse response = profilePhotoService.uploadProfilePhoto(email, file);
        return responseHandlers.handleResponse(response);
    }

    @PatchMapping(value = "/update-profilePhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> updateProfilePhoto(@AuthenticationPrincipal UserDetails currentUser,
                                                           @RequestParam("file") MultipartFile file){
        BaseResponse response = profilePhotoService.updateProfilePhoto(currentUser.getUsername(), file);
        return responseHandlers.handleResponse(response);
    }

    @GetMapping("/get-profilePhoto")
    public ResponseEntity<?> getProfilePhoto(@AuthenticationPrincipal UserDetails currentUser) {
        String username = currentUser.getUsername();
        BaseResponse response = profilePhotoService.getProfileImage(username);

        if (response.getStatusCode() == HttpServletResponse.SC_OK) {

            Resource resource = (Resource) response.getData();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + resource.getFilename() + "\"")
                    .body(response.getData());
        } else{
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(response.getMessage());
        }
    }

    @GetMapping("/get-profilePhoto/{username}")
    public ResponseEntity<?> getProfilePhotoByUsername(@PathVariable String username){
        BaseResponse response = profilePhotoService.getProfilePhotoByUsername(username);

        if (response.getStatusCode() == HttpServletResponse.SC_OK) {

            Resource resource = (Resource) response.getData();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + resource.getFilename() + "\"")
                    .body(response.getData());
        } else{
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(response.getMessage());
        }
    }

}
