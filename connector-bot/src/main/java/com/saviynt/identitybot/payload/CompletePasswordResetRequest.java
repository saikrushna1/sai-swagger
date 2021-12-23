package com.saviynt.identitybot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletePasswordResetRequest {

    @NotBlank
    private String password;

    @NotBlank
    private String token;

}
