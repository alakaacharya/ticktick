package com.example.finalassignment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finalassignment.R;

public class AboutActivity extends AppCompatActivity {
    private Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btnMap=findViewById(R.id.btnMap);

        btnMap.setOnClickListener(v -> {
         Intent intent=new Intent(AboutActivity.this,LocationActivity.class)   ;
         startActivity(intent);
        });


    }
}
