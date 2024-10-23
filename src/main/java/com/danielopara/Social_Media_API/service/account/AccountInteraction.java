package com.danielopara.Social_Media_API.service.account;

import com.danielopara.Social_Media_API.response.BaseResponse;

public interface AccountInteraction {
    void followAccount(String email, Long accountId);
    BaseResponse unfollowAccount(String email, Long accountId);
}
