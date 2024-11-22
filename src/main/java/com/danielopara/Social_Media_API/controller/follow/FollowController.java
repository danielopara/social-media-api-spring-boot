package com.danielopara.Social_Media_API.controller.follow;

import com.danielopara.Social_Media_API.response.BaseResponse;
import com.danielopara.Social_Media_API.service.account.implementation.AccountService;
import com.danielopara.Social_Media_API.utils.ResponseHandlers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Follow Management", description = "follow management")
@RequestMapping("/api/v1/social/follow")
@RequiredArgsConstructor
@RestController
public class FollowController {
    private final AccountService accountService;
    private final ResponseHandlers responseHandlers;

    @Operation(summary = "Follow an account", description = "Allows the authenticated user to follow another account.")
    @PostMapping("/accounts/follow")
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

    @Operation(summary = "Unfollow an account", description = "Allows the authenticated user to unfollow an account.")
    @PostMapping("/accounts/unfollow")
    ResponseEntity<?> unfollow(@AuthenticationPrincipal UserDetails currentUser, @RequestBody Long accountId){
        BaseResponse response = accountService.unfollowAccount(currentUser.getUsername(), accountId);
        return responseHandlers.handleResponse(response);
    }

    @Operation(summary = "Get following list", description = "Fetches a list of accounts followed by the authenticated user.")
    @GetMapping("/getFollowing")
    ResponseEntity<?> getFollowing(@AuthenticationPrincipal UserDetails currentUser){
        BaseResponse response = accountService.listFollowingAccountByAccountId(currentUser.getUsername());
        return responseHandlers.handleResponse(response);
    }
}
