package com.example.finalassignment.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.R;
import com.example.finalassignment.models.User;
import com.raywenderlich.android.validatetor.ValidateTor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finalassignment.API.ApiClient.getClient;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String USER_NAME = "USER_NAME";
    private static int  PRIVATE_MODE = 0;
    private static String USER_ID = "USER_ID";
    private EditText etUsername, etPassword;
    private Button btnLogin;
    ValidateTor validateTor = new ValidateTor();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {
            validateField();
            networkCall();
        });

    }

    private void networkCall() {

//        final LoginBLL bll = new LoginBLL(etUsername.getText().toString(), etPassword.getText().toString());
//        StrictMod.StrictMode();

//        if (bll.checkUser()) {
//            MySharedPrefernces.setBoolean(getApplicationContext(), "isUserLoggedIn", true);
//            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//            startActivity(intent);
//        } else {
//            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
//        }


        APIEndpoints apiInterface = getClient().create(APIEndpoints.class);
        User.User_ user_ = new User.User_();


        user_.setUsername(etUsername.getText().toString().trim());
        user_.setPassword(etPassword.getText().toString().trim());


        Call<User> userCall = apiInterface.login(user_);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    MySharedPrefernces.setBoolean(getApplicationContext(), "isUserLoggedIn", true);
                    finish();
                    if (response.body().getStatus().equals("success")) {
//                        User user=new User();
                        User.User_ user = (response.body().getData().getUser());
                        initSharedPref(user);
                        Log.d(TAG, "onResponse: " + user.toString());
                        Toast toast = Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "login unsuccessful", Toast.LENGTH_SHORT);
            }
        });
    }

    private void initSharedPref(User.User_ user) {
        SharedPreferences sharedPref= getSharedPreferences(USER_ID, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_ID, user.getId());
        editor.putString(USER_NAME, user.getFirstname());
        editor.apply();
    }

    private void validateField() {
        if (validateTor.isEmpty(etUsername.getText().toString())) {
            etUsername.setError("Field is empty!");
        }
        if (validateTor.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Field is empty!");
        }
    }

    public void openRegisterActivity(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}