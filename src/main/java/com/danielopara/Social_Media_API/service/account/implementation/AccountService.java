package com.danielopara.Social_Media_API.service.account.implementation;

import com.danielopara.Social_Media_API.Repository.AccountRepository;
import com.danielopara.Social_Media_API.Repository.FollowRepository;
import com.danielopara.Social_Media_API.Repository.UserRepository;
import com.danielopara.Social_Media_API.dto.AccountDTO;
import com.danielopara.Social_Media_API.dto.CreateAccountDto;
import com.danielopara.Social_Media_API.models.Account;
import com.danielopara.Social_Media_API.models.Follow;
import com.danielopara.Social_Media_API.models.UserModel;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.AccountInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountInterface {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
//    private final BaseResponse response;

    private static final String ACCOUNT_NOT_FOUND = "Account not found";
    private static final String INTERNAL_ERROR = "Internal Server Error";
    @Override
    public BaseResponse createAccount(String email, CreateAccountDto accountDto) {
        try{

            validateUserEmail(email);

            if(accountRepository.findByEmail(email).isPresent()){
                throw new RuntimeException("email used");
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
            return createErrorResponse(e);
        }
    }

    @Override
    public BaseResponse getAccountByAccountId(Long id) {
        try{
            Account account = getAccountById(id);

            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setDisplayName(account.getDisplayName());
            accountDTO.setUsername(account.getUsername());
            accountDTO.setFollowers((long) account.getFollowers().size());
            accountDTO.setFollowing((long) account.getFollowing().size());


            return new BaseResponse(HttpServletResponse.SC_OK,
                    "User found",
                    accountDTO);

        } catch(Exception e) {
            return createErrorResponse(e);
        }
    }

    @Override
    public BaseResponse getAccountByUsername(String username) {
        try{
            Account account = findAccountByUsername(username);

            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setDisplayName(account.getDisplayName());
            accountDTO.setUsername(account.getUsername());
            accountDTO.setFollowers((long) account.getFollowers().size());
            accountDTO.setFollowing((long) account.getFollowing().size());


            return new BaseResponse(HttpServletResponse.SC_OK,
                    "User found",
                    accountDTO);
        }catch (Exception e){
            return createErrorResponse(e);
        }
    }

    @Override
    public BaseResponse allAccounts() {
        try{
            List<Map<String, Object>> userAccount = new ArrayList<>();
            List<Account> allAccounts = accountRepository.findAll();

            for(Account accountDTO : allAccounts){
                Map<String, Object> account = new HashMap<>();
                account.put("id", accountDTO.getId());
                account.put("username", accountDTO.getUsername());
                account.put("email", accountDTO.getEmail());

                userAccount.add(account);
            }


            return BaseResponse.createSuccessResponse("success", userAccount);
        } catch (Exception e){
            return createErrorResponse(e);
        }
    }

    @Override
    @Transactional
    public void followAccount(String email, Long accountId) {
        Account follower = accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("account not found"));
        Account following = accountRepository.findById(accountId).orElseThrow(()->new RuntimeException("account not found"));

        validateFollowOperation(follower, following);

        if(followRepository.existsByFollowerAndFollowing(follower, following)){
            throw new RuntimeException("You are already following this account");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        // Add the relationship bidirectionally
        follower.addFollowing(follow);
        following.addFollower(follow);

        // Save the follow relationship
        followRepository.save(follow);
    }

    @Transactional
    @Override
    public BaseResponse unfollowAccount(String email, Long accountId) {
        try{
           Account follower = accountRepository.findByEmail(email).orElseThrow(()->new RuntimeException("account not found"));
           Account following = accountRepository.findById(accountId).orElseThrow(()-> new RuntimeException("account not found"));

           validateFollowOperation(follower, following);

           Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                   .orElseThrow(() -> new RuntimeException("Follow relationship does not exist"));


            followRepository.deleteById(follow.getId());

            follower.getFollowing().remove(follow);
            following.getFollowers().remove(follow);

            followRepository.delete(follow);

            accountRepository.save(follower);
            accountRepository.save(following);

            return BaseResponse.createSuccessResponse("unfollowed", null);
        }catch (Exception e){
            return createErrorResponse(e);
        }
    }

    @Override
    public BaseResponse listFollowingAccountByAccountId(String email) {
        try {
            List<Map<String, Object>> following = new ArrayList<>();

            Account userAccount  = getAccountByEmail(email);

            for (Follow follow : userAccount.getFollowing()) {
                Account followedAccount = follow.getFollowing();

                if (followedAccount == null) {
                    // Log the issue and skip this follow if followedAccount is null
                    continue;
                }

                Map<String, Object> followerDetails = new HashMap<>();
                followerDetails.put("id", followedAccount.getId());
                followerDetails.put("username", followedAccount.getUsername());
                following.add(followerDetails);
            }

            return BaseResponse.createSuccessResponse("Success", following);

        } catch (NullPointerException ex) {
            // Handle specific NullPointerExceptions if they occur (e.g., accessing null fields)
            return BaseResponse.createErrorResponse("Null Pointer Exception: " + ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            // Log and handle general exceptions
            ex.printStackTrace();  // Log the full stack trace for debugging purposes
            return BaseResponse.createErrorResponse("Internal Server Error: " + ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public BaseResponse listFollowerAccountByAccountEmail(String email) {
        try{
            List<Map<String, Object>> followers = new ArrayList<>();

            Account account = getAccountByEmail(email);

            for(Follow follow: account.getFollowers()){
                Account followerAccount = follow.getFollower();

                Map<String, Object> followingDetails = new HashMap<>();
                followingDetails.put("id", followerAccount.getId());
                followingDetails.put("username", followerAccount.getUsername());

                followers.add(followingDetails);
            }

            return BaseResponse.createSuccessResponse("success", followers);

        } catch (Exception e){
            return createErrorResponse(e);
        }
    }

    private void validateFollowOperation(Account follower, Account following){
        if(Objects.equals(follower.getId(), following.getId())){
            throw new RuntimeException("you cannot follow/unfollow yourself");
        }
    }

    private Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found."));
    }

    private Account findAccountByUsername(String username){
        return accountRepository.findByUsername(username).
                orElseThrow(()-> new RuntimeException("Account not found"));
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found."));
    }

    private void validateUserEmail(String email){
        if(userRepository.findByEmail(email).isEmpty()){
            throw new RuntimeException("email does not exist");
        }
    }

    private BaseResponse createErrorResponse(Exception e){
        return BaseResponse.createErrorResponse(INTERNAL_ERROR, e.getMessage());
    }



}
