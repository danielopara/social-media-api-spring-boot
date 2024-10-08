package com.danielopara.Social_Media_API.Repository;

import com.danielopara.Social_Media_API.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
