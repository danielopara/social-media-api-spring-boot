package com.danielopara.Social_Media_API.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private int statusCode;
    private String message;
    private Object data;


    public BaseResponse createSuccessResponse(String message, Object data ){
        return new BaseResponse(HttpServletResponse.SC_OK, message, data);
    }

    public BaseResponse createErrorResponse(String message, Object data){
        return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, data);
    }
}
