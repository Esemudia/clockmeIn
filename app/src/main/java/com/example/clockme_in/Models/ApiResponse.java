package com.example.clockme_in.Models;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("users")
    private Users users;
    @SerializedName("message")
    private String message;
    public Users getUser()
    {
        return users;
    }

    public String getMessage() {
        return message;
    }
}
