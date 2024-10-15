package com.danielopara.Social_Media_API.service.account.implementation;

import com.danielopara.Social_Media_API.Repository.AccountRepository;
import com.danielopara.Social_Media_API.Repository.UserRepository;
import com.danielopara.Social_Media_API.dto.CreateAccountDto;
import com.danielopara.Social_Media_API.models.Account;
import com.danielopara.Social_Media_API.models.UserModel;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.AccountInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountInterface {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Override
    public BaseResponse account(CreateAccountDto accountDto) {
        try{
            String email = accountDto.getEmail();
            Optional<UserModel> userEmail = userRepository.findByEmail(email);
            if(userEmail.isEmpty()){
                return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST,
                        "email does not exist",
                        null);
            }

            Optional<Account> accountEmail = accountRepository.findByEmail(email);
            if(accountEmail.isPresent()){
                return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST,
                        "email used", null);
            }

            Account account = new Account();
            account.setUsername(account.getUsername());
            account.setDisplayName(accountDto.getDisplayName());
            account.setEmail(account.getEmail());
            account.setFollowers(0L);
            account.setFollowing(0L);

            return new BaseResponse(
                    HttpServletResponse.SC_OK,
                    "account created",
                    account
            );
        } catch (Exception e){
            return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal Server Error",
                    e.getMessage());
        }
    }
}
