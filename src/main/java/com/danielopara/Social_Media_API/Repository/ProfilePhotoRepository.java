package com.danielopara.Social_Media_API.Repository;

import com.danielopara.Social_Media_API.models.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {
   Optional<ProfilePhoto> findByAccountId(Long id);
}
