package com.danielopara.Social_Media_API.Repository;

import com.danielopara.Social_Media_API.models.Account;
import com.danielopara.Social_Media_API.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(Account follower, Account following);
    Optional<Follow> findByFollowerId(Long id);
    boolean existsByFollowerAndFollowing(Account follower, Account following);
}
