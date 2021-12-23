package com.saviynt.identitybot.payload;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class UserUpdateDto {

    private String firstName;

    private String lastName;

    private String password;

  }
