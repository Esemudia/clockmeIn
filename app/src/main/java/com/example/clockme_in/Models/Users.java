package com.example.clockme_in.Models;

public class Users {
    private String user_id;
    private String fullname;
    private String email;
    private String phone;

    public Users(String userId, String fullname, String email, String phone) {
        user_id = userId;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }


}
