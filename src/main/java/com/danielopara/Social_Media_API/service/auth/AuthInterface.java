package com.danielopara.Social_Media_API.service.auth;

import com.danielopara.Social_Media_API.dto.LoginDto;
import com.danielopara.Social_Media_API.response.BaseResponse;

public interface AuthInterface {
    BaseResponse accountLogin(LoginDto login);
}
