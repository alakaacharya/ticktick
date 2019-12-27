package com.example.finalassignment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.R;
import com.example.finalassignment.models.RegisterResponse;
import com.example.finalassignment.models.User;
import com.raywenderlich.android.validatetor.ValidateTor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finalassignment.API.ApiClient.getClient;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    EditText firstName;
    EditText lastName;
    EditText userName;
    EditText password;

    Button register;
    ValidateTor validateTor = new ValidateTor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName=findViewById(R.id.etFirstname);
        lastName=findViewById(R.id.etLastname);
        userName=findViewById(R.id.etUsername);
        password=findViewById(R.id.etPassword);



        register=findViewById(R.id.btnRegister);

        register.setOnClickListener(v -> {
            validateFields();
            networkCall();


        });


    }

    private void networkCall() {

        APIEndpoints apiInterface = getClient().create(APIEndpoints.class);
        User.User_ user_ = new User.User_();
        user_.setFirstname(firstName.getText().toString().trim());
        user_.setLastname(lastName.getText().toString().trim());
        user_.setUsername(userName.getText().toString().trim());
        user_.setPassword(password.getText().toString().trim());


        Call<RegisterResponse> userCall=apiInterface.signUp(user_);
        userCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,""+response.body().getMessage(),Toast.LENGTH_LONG).show();
                    if (response.body().getStatus().equals("success")){
                        Toast toast= Toast.makeText(RegisterActivity.this, "sign up successful", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

            }
        });

    }

    private void validateFields() {
        if (validateTor.isEmpty(firstName.getText().toString())) {
            firstName.setError("Field is empty!");
        }
        if (validateTor.isEmpty(lastName.getText().toString()) )
        {
            lastName.setError("Field is empty!");
        }
        if (validateTor.isEmpty(userName.getText().toString())) {
            userName.setError("Field is empty!");
        }
        if (validateTor.isEmpty(password.getText().toString())) {
            password.setError("Field is empty!");
        }
    }
}
