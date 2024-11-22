package com.danielopara.Social_Media_API.Repository;

import com.danielopara.Social_Media_API.models.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {
}
