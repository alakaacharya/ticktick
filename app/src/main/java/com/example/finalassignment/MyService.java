package com.example.finalassignment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.models.Datum;
import com.example.finalassignment.models.Tasks;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finalassignment.API.ApiClient.getClient;

public class MyService extends Service {

    private String notifyDate;
    private List<Datum> datumList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        APIEndpoints apiInterface = getClient().create(APIEndpoints.class);
        apiInterface.getAllTasks().enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.isSuccessful()) {
                    datumList = response.body().getData();
                    start();
                }
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {

            }
        });
        return START_STICKY;
    }

    private void start() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        while (true) {
            for (Datum datum : datumList) {
                String todayDate = dateFormat.format(System.currentTimeMillis());
                if (todayDate.equals(datum.getDueDate()) && !notifyDate.equals(todayDate)) {
                    showNotification("Reminder", datum.getTaskName());
                    notifyDate = todayDate;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void showNotification(String title, String desc) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Channel1";
            String description = "This is channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel1", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(desc)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(1, builder.build());

    }

}


