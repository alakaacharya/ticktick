package com.example.finalassignment.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.raywenderlich.android.validatetor.ValidateTor;

import java.util.Calendar;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finalassignment.API.ApiClient.getClient;

public class EditTextActivity extends AppCompatActivity {
    private static final String TAG = "EditTextActivity";
    String task_id="1";
    EditText taskName;
    TextView timeView;
    TextView dateView;
    ImageView denyButton, allowButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ValidateTor validateTor = new ValidateTor();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtasks);
        denyButton = findViewById(R.id.denyButton);
        allowButton = findViewById(R.id.okayButton);
        dateView = findViewById(R.id.dateTextView);
        taskName = findViewById(R.id.etentertask);
        timeView = findViewById(R.id.timeTextView);

        Bundle intent=getIntent().getExtras();
        if (intent!=null){
           String taskDescription= (String) intent.get("TASK_DETAIL");
           String task_time= (String) intent.get("TASK_TIME");
           String task_date= (String) intent.get("TASK_DATE");
            task_id= (String) intent.get("TASK_ID");

            taskName.setText(taskDescription);
            timeView.setText(task_time);
            dateView.setText(task_date);
//            Log.d(TAG, "onCreate: "+taskName+taskDate+taskTime);
        }
        timeView.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(EditTextActivity.this, (timePicker, selectedHour, selectedMinute) ->
                    timeView.setText(selectedHour + ":" + selectedMinute), hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        /*datepicker*/
        dateView.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    EditTextActivity.this,
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
            dateView.setText(date);
        };
        allowButton.setOnClickListener(v -> {
            validateFields();
            sendDataToServer();

        });
        denyButton.setOnClickListener(v->{
            Intent intent1=new Intent(EditTextActivity.this,DashboardActivity.class);
            startActivity(intent1);

        });
    }
    private void validateFields() {
        if (validateTor.isEmpty(taskName.getText().toString())) {
            taskName.setError("empty task field");
        }
        if (validateTor.isEmpty(dateView.getText().toString())) {
            dateView.setError("please select the due date");
        }
        if (validateTor.isEmpty(timeView.getText().toString())) {
            timeView.setError("please select the due time");
        }
    }

    private void sendDataToServer() {
        Datum datum = new Datum();
        datum.setTaskId(task_id);
        datum.setTaskName(taskName.getText().toString().trim());
        datum.setDueDate(dateView.getText().toString().trim());
        datum.setDueTime(timeView.getText().toString().trim());
        datum.setTaskStatus(false);

        /*do network call*/
        APIEndpoints apiInterface = getClient().create(APIEndpoints.class);
        apiInterface.updateTask(task_id,datum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(EditTextActivity.this,"updated successfully",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(EditTextActivity.this,DashboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
