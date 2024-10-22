package com.danielopara.Social_Media_API.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountDTO {
    private String displayName;
    private String username;
    private Long followers;
    private Long following;
}
