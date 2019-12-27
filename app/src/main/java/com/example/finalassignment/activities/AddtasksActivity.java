package com.example.finalassignment.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.R;
import com.example.finalassignment.models.Datum;
import com.example.finalassignment.models.Tasks;
import com.raywenderlich.android.validatetor.ValidateTor;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finalassignment.API.ApiClient.getClient;

public class AddtasksActivity extends AppCompatActivity {
    private static int  PRIVATE_MODE = 0;
    private static String PREF_NAME = "USER_ID";
    ImageView denyButton, allowButton;
    TextView dateText, timeTextView;
    EditText taskDetail;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ValidateTor validateTor = new ValidateTor();
    String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref= getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        user_id= sharedPref.getString(PREF_NAME,"user_id");
        setContentView(R.layout.activity_addtasks);
        denyButton = findViewById(R.id.denyButton);
        allowButton = findViewById(R.id.okayButton);
        dateText = findViewById(R.id.dateTextView);
        taskDetail = findViewById(R.id.etentertask);
        timeTextView = findViewById(R.id.timeTextView);

        denyButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddtasksActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
        /*allow button*/
        allowButton.setOnClickListener(v -> {
            validateFields();
            sendDataToServer();
            Intent intent = new Intent(AddtasksActivity.this, DashboardActivity.class);
            startActivity(intent);

        });

        timeTextView.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddtasksActivity.this, (timePicker, selectedHour, selectedMinute) ->
                    timeTextView.setText(selectedHour + ":" + selectedMinute), hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        /*datepicker*/
        dateText.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AddtasksActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Log.d("TAG", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
            String monthString= String.valueOf(month);
            String dayString= String.valueOf(day);
            if (month<10){
//                month= Integer.parseInt("0"+month);
                monthString="0"+month;

            }
            if (day<10){
//                day= Integer.parseInt("0"+day);
                dayString="0"+day;

            }
            String date = year + "-" + monthString + "-" + dayString;
            dateText.setText(date);
        };
    }

    private void sendDataToServer() {
        Datum datum = new Datum();
        datum.setTaskName(taskDetail.getText().toString().trim());
        datum.setDueDate(dateText.getText().toString().trim());
        datum.setDueTime(timeTextView.getText().toString().trim());
        datum.setTaskStatus(false);
        datum.setUserId(user_id);

        /*do network call*/
        APIEndpoints apiInterface = getClient().create(APIEndpoints.class);
        apiInterface.addTask(datum).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                Intent intent = new Intent(AddtasksActivity.this, DashboardActivity.class);
                startActivity(intent);
                if (response.isSuccessful()) {

                    assert response.body() != null;
                    if (response.body().getMessage().equals("New task created!")) {
                        Toast.makeText(AddtasksActivity.this, "New Task added Successfully", Toast.LENGTH_LONG).show();
                        intent = new Intent(AddtasksActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {

            }
        });

    }

    private void validateFields() {
        if (validateTor.isEmpty(taskDetail.getText().toString())) {
            taskDetail.setError("empty task field");
        }
        if (validateTor.isEmpty(dateText.getText().toString())) {
            dateText.setError("please select the due date");
        }
        if (validateTor.isEmpty(timeTextView.getText().toString())) {
            timeTextView.setError("please select the due time");
        }
    }
}