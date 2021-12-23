package com.saviynt.identitybot.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateSuccessResppnse {
    private String message;
    private Long id;

}
