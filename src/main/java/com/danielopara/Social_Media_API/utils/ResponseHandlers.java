package com.danielopara.Social_Media_API.utils;

import com.danielopara.Social_Media_API.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandlers {
    public ResponseEntity<BaseResponse> handleResponse(BaseResponse response) {
        return response.getStatusCode() == HttpServletResponse.SC_OK
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
