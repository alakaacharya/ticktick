package com.example.finalassignment.BLL;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.API.ApiClient;
import com.example.finalassignment.models.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginBLL {
    private String username;
    private String password;
    boolean issuccess = false;

    public LoginBLL(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public boolean checkUser() {
        User.User_ user_ = new User.User_();
        user_.setUsername(username);
        user_.setPassword(password);
        Call<User> loginResponseCall = ApiClient.getClient().create(APIEndpoints.class).login(user_);
        try {
            Response<User> loginResponseResponse = loginResponseCall.execute();
            if (loginResponseResponse.body().getStatus().equals("success")) {
                ApiClient.cookie = loginResponseResponse.headers().get("set-cookie");
                issuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return issuccess;
    }
}
