package com.danielopara.Social_Media_API.service.account;

import com.danielopara.Social_Media_API.response.BaseResponse;

public interface AccountRetrieval {
    BaseResponse getAccountByAccountId(Long id);
    BaseResponse getAccountByUsername(String username);
}
