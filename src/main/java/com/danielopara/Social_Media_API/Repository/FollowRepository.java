package com.danielopara.Social_Media_API.Repository;

import com.danielopara.Social_Media_API.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
