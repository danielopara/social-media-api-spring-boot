package com.danielopara.Social_Media_API.controller.account;

import com.danielopara.Social_Media_API.dto.CreateAccountDto;
import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.implementation.AccountService;
import com.danielopara.Social_Media_API.utils.ResponseHandlers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Management", description = "Endpoints for managing user accounts")
@RequestMapping("/api/v1/social")
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ResponseHandlers responseHandlers;


    @Operation(summary = "Create a new account", description = "Creates an account for the authenticated user.")
    @PostMapping("/createAccount")
    ResponseEntity<BaseResponse> createAccount(@AuthenticationPrincipal UserDetails currentUser, @RequestBody CreateAccountDto createAccountDto){
        String email = currentUser.getUsername();
        BaseResponse response = accountService.createAccount(email, createAccountDto);
        return responseHandlers.handleResponse(response);
    }

    @Operation(summary = "Get account by ID", description = "Fetches account details by the account's ID.")
    @GetMapping("/accounts/{accountId}")
    ResponseEntity<?> getAccountById(@PathVariable Long accountId){
        BaseResponse response = accountService.getAccountByAccountId(accountId);
        return responseHandlers.handleResponse(response);
    }

    @Operation(summary = "Get account by username", description = "Fetches account details by the username.")
    @GetMapping("/accounts/username/{username}")
    ResponseEntity<?> getAccountByUsername(@PathVariable String username){
        BaseResponse response = accountService.getAccountByUsername(username);
        return responseHandlers.handleResponse(response);
    }



    @Operation(summary = "Get followers list", description = "Fetches a list of followers for the authenticated user.")
    @GetMapping("/getFollowers")
    ResponseEntity<?> getFollowers(@AuthenticationPrincipal UserDetails currentUser){
        BaseResponse response = accountService.listFollowerAccountByAccountEmail(currentUser.getUsername());
        return responseHandlers.handleResponse(response);
    }

    @Operation(summary = "Get all accounts", description = "Fetches a list of all accounts in the system.")
    @GetMapping("/accounts")
    ResponseEntity<?> getAllAccounts(){
        BaseResponse response = accountService.allAccounts();
        return responseHandlers.handleResponse(response);
    }
}
