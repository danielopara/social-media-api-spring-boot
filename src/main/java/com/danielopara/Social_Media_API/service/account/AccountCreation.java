package com.danielopara.Social_Media_API.service.account;

import com.danielopara.Social_Media_API.dto.CreateAccountDto;
import com.danielopara.Social_Media_API.response.BaseResponse;

public interface AccountCreation {
    BaseResponse account(CreateAccountDto accountDto);
}
