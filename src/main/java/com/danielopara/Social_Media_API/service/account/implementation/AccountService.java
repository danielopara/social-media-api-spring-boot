package com.danielopara.Social_Media_API.service.account.implementation;

import com.danielopara.Social_Media_API.Repository.AccountRepository;
import com.danielopara.Social_Media_API.Repository.UserRepository;
import com.danielopara.Social_Media_API.dto.AccountDTO;
import com.danielopara.Social_Media_API.dto.CreateAccountDto;
import com.danielopara.Social_Media_API.models.Account;
import com.danielopara.Social_Media_API.models.UserModel;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.AccountInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountInterface {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Override
    public BaseResponse createAccount(String email, CreateAccountDto accountDto) {
        try{

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
            account.setUsername(accountDto.getUsername());
            account.setDisplayName(accountDto.getDisplayName());
            account.setEmail(email);
            account.setFollowers(new HashSet<>());
            account.setFollowing(new HashSet<>());

            accountRepository.save(account);

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

    @Override
    public BaseResponse getAccountByAccountId(Long id) {
        try{
            Optional<Account> accountId = accountRepository.findById(id);
            if(accountId.isEmpty()){
                return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST,
                        "User not found",
                        null);
            }

            Account account = accountId.get();

            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setDisplayName(account.getDisplayName());
            accountDTO.setUsername(account.getUsername());
            accountDTO.setFollowers((long) account.getFollowers().size());
            accountDTO.setFollowing((long) account.getFollowing().size());

//            Map<String, Object> accountDetails = new HashMap<>();
//            accountDetails.put("displayName", account.getDisplayName());
//            accountDetails.put("username", account.getUsername());
//            accountDetails.put("followers", account.getFollowers().size());
//            accountDetails.put("following", account.getFollowing().size());

            return new BaseResponse(HttpServletResponse.SC_OK,
                    "User found",
                    accountDTO);

        } catch(Exception e) {
            return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal Server Error",
                    e.getMessage());
        }
    }

    @Override
    public BaseResponse getAccountByUsername(String username) {
        return null;
    }
}
