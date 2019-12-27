package com.example.finalassignment.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.finalassignment.R;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Object List;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        getTasks();
    }

    private void getTasks() {
//        ApiClient.getEndPoints().getTasks().enqueue(new Callback<List<TaskResponse>> {
//
//            public void onResponse
//            (Call < List < TaskResponse >> call, Response < List < TaskResponse >> response){
//                TaskAdapters adapter = new TaskAdapters( MainActivity.this, response.body());
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//            }
//            public void onFailure(Call<List < TaskResponse >> call, Throwable t){
//            }
//        });
    }
}
