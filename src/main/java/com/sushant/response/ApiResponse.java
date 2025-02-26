package com.sushant.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;



public class ApiResponse {

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
