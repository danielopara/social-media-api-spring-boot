package com.danielopara.Social_Media_API.service.account.profilePhoto.profileService;

import com.danielopara.Social_Media_API.Repository.AccountRepository;
import com.danielopara.Social_Media_API.Repository.ProfilePhotoRepository;
import com.danielopara.Social_Media_API.models.Account;
import com.danielopara.Social_Media_API.models.ProfilePhoto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.profilePhoto.profileInterface.ProfilePhotoInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService implements ProfilePhotoInterface {
    private final AccountRepository accountRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
//    private final BaseResponse response;

    private static final String INTERNAL_SERVER = "Internal Server Error";
    @Override
    public BaseResponse uploadProfilePhoto(String email, MultipartFile image) {
        try{
            Account account = getAccountByEmail(email);

            checkProfilePhotoExists(account.getId());

            String folder = System.getProperty("user.dir") + "/profile_images/";
            String filePath = folder + account.getUsername() + ".jpg";

            File directory = new File(folder);
            if(!directory.exists()){
                directory.mkdirs();
            }

            image.transferTo(new File(filePath));

            ProfilePhoto photo = new ProfilePhoto();
            photo.setAccount(account);
            photo.setFilePath(filePath);
            photo.setFileName(account.getUsername() + " profile-photo");
            photo.setFileSize(image.getSize());

            profilePhotoRepository.save(photo);

            return BaseResponse.createSuccessResponse("uploaded successfully", photo.getFileName());

        } catch(Exception e){
            return BaseResponse.createErrorResponse(INTERNAL_SERVER, e.getMessage());
        }
    }

    @Override
    public BaseResponse getProfileImage(String email) {
        try{
            Account account = getAccountByEmail(email);

            ProfilePhoto photo = profilePhotoRepository.findByAccountId(account.getId())
                    .orElseThrow(() -> new RuntimeException("profile photo not found"));

            File file = new File(photo.getFilePath());
            Resource resource = new FileSystemResource(file);

            return BaseResponse.createSuccessResponse("success", resource);
        }catch (Exception e){
            return BaseResponse.createErrorResponse(INTERNAL_SERVER, e.getMessage());
        }
    }

    @Override
    public BaseResponse updateProfilePhoto(String email, MultipartFile image) {
        try{
            Account account = getAccountByEmail(email);

            ProfilePhoto existingUser = getProfilePhotoAccount(account.getId());

            String folder = System.getProperty("user.dir") + "/profile_images/";
            String filePath = folder + account.getUsername() + ".jpg";

            File directory = new File(folder);
            if(!directory.exists()){
                directory.mkdirs();
            }

            image.transferTo(new File(filePath));

            existingUser.setFilePath(filePath);
            existingUser.setFileName(account.getUsername() + " profile-photo");
            existingUser.setFileSize(image.getSize());

            profilePhotoRepository.save(existingUser);

            return BaseResponse.createSuccessResponse("profile photo updated successfully", existingUser.getFileName());

        } catch(Exception e){
            return BaseResponse.createErrorResponse(INTERNAL_SERVER, e.getMessage());
        }
    }

    @Cacheable("profilePhotos")
    private ProfilePhoto getProfilePhotoAccount(Long id){
        return profilePhotoRepository.findByAccountId(id)
                .orElseThrow(()->new RuntimeException("profile photo not found"));
    }

    private void checkProfilePhotoExists(Long id){
        profilePhotoRepository.findByAccountId(id)
                .ifPresent(profilePhoto -> {throw new RuntimeException("user has profile photo");});
    }

    private Account getAccountById(Long id){
        return accountRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Account not found"));
    }

    @Cacheable("users")
    private Account getAccountByEmail(String email){
        return accountRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Account not found"));
    }
}
