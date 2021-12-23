package com.saviynt.identitybot.exceptions;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ErrorResponse {

    private String message;

    private int status ;

    private String error;



}
