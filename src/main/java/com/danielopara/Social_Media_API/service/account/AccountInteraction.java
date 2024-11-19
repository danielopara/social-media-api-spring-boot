package com.danielopara.Social_Media_API.service.account;

import com.danielopara.Social_Media_API.models.Account;
import com.danielopara.Social_Media_API.response.BaseResponse;

import java.util.List;

public interface AccountInteraction {
    void followAccount(String email, Long accountId);
    BaseResponse unfollowAccount(String email, Long accountId);
    BaseResponse listFollowingAccountByAccountId(String email);
    BaseResponse listFollowerAccountByAccountEmail(String email);
}
