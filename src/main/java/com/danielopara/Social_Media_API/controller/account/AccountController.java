package com.danielopara.Social_Media_API.controller.account;

import com.danielopara.Social_Media_API.dto.CreateAccountDto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.implementation.AccountService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/social")
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/createAccount")
    ResponseEntity<?> createAccount(@AuthenticationPrincipal UserDetails currentUser, @RequestBody CreateAccountDto createAccountDto){
        String email = currentUser.getUsername();
        BaseResponse response = accountService.createAccount(email, createAccountDto);
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-account/{accountId}")
    ResponseEntity<?> getAccountById(@PathVariable Long accountId){
        BaseResponse response = accountService.getAccountByAccountId(accountId);
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-account-byUsername/{username}")
    ResponseEntity<?> getAccountByUsername(@PathVariable String username){
        BaseResponse response = accountService.getAccountByUsername(username);
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/follow")
    ResponseEntity<BaseResponse> followAccount(@AuthenticationPrincipal UserDetails currentUser, @RequestBody Long accountId){
        try{
            String accountEmail = currentUser.getUsername();

            accountService.followAccount(accountEmail, accountId);
            return ResponseEntity.ok(new BaseResponse(HttpServletResponse.SC_OK,
                    "followed", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Internal Server Error",
                            e.getMessage()));
        }
    }

    @PostMapping("/unfollow")
    ResponseEntity<?> unfollow(@AuthenticationPrincipal UserDetails currentUser, @RequestBody Long accountId){
        BaseResponse response = accountService.unfollowAccount(currentUser.getUsername(), accountId);
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
        }
    }

    @GetMapping("/getFollowing")
    ResponseEntity<?> getFollowing(@AuthenticationPrincipal UserDetails currentUser){
        BaseResponse response = accountService.listFollowingAccountByAccountId(currentUser.getUsername());
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getFollowers")
    ResponseEntity<?> getFollowers(@AuthenticationPrincipal UserDetails currentUser){
        BaseResponse response = accountService.listFollowerAccountByAccountEmail(currentUser.getUsername());
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    ResponseEntity<?> getAllAccounts(){
        BaseResponse response = accountService.allAccounts();
        if(response.getStatusCode() == HttpServletResponse.SC_OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
        }
    }
}
