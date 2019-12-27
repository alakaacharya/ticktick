package com.example.finalassignment.BLL;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.API.ApiClient;
import com.example.finalassignment.models.RegisterResponse;
import com.example.finalassignment.models.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterBLL {

        private String username;
        private String password;
        private String fname;
        private String lname;

        boolean issuccess=false;

    public RegisterBLL(String username,String password,String fname, String lname){
            this.username=username;
            this.password=password;
            this.fname=fname;
            this.lname=lname;
        }


        public boolean addUser() {
//        APIEndpoints apiEndpoints= ApiClient.getClient().create(APIEndpoints.class);

            Call<RegisterResponse> loginResponseCall = ApiClient.getClient().create(APIEndpoints.class).signUp(new User.User_());

            try {
                Response<RegisterResponse> loginResponseResponse = loginResponseCall.execute();
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
