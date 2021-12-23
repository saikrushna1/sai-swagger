package com.saviynt.identitybot.payload;

import lombok.Data;

@Data
public class ApiResponse {

    private Boolean success;
    private Object data;
    private String message;

    public ApiResponse(Object data, Boolean success, String message) {
        this.success = success;
        this.data = data;
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
