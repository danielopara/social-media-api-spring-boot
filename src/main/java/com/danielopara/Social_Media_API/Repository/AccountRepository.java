package com.danielopara.Social_Media_API.Repository;

import com.danielopara.Social_Media_API.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
