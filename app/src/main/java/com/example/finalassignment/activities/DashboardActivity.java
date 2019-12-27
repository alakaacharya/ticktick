package com.example.finalassignment.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.R;
import com.example.finalassignment.adapters.TaskAdapters;
import com.example.finalassignment.models.Datum;
import com.example.finalassignment.models.Tasks;
import com.example.finalassignment.models.User;
import com.example.finalassignment.utils.ShakeDetector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finalassignment.API.ApiClient.getClient;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";
    private static final String CHANNEL_ID = "channel_id";
    private static int  PRIVATE_MODE = 0;
    private static String PREF_NAME = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private String notifyDate;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nv;

    private SensorManager manager;
    private Sensor sensor;
    private SensorEventListener event;


    String user_id;
    String userName;

    //TaskAdapters taskAdapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref= getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        user_id= sharedPref.getString(PREF_NAME,"user_id");
        userName= sharedPref.getString(USER_NAME,"user_name");
        userName="Welcome,"+userName;
        setContentView(R.layout.activity_dashboard);
        initView();
        createNotificationChannel();
        doNetwork();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            doNetwork();
            swipeRefreshLayout.setRefreshing(false);
        });
        // recyclerView.setAdapter(mAdapter);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        event = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] == 0) {
                    startActivity(new Intent(DashboardActivity.this, AddtasksActivity.class));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
//            tvShake.setText("Shake Action is just detected!!");
                Toast.makeText(DashboardActivity.this, "Shaked!!!", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                doNetwork();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);






        /*navbar*/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nav_view);
        View headerView=nv.getHeaderView(0);
        TextView welcomeUserTextView=headerView.findViewById(R.id.welcomeTextView);
        welcomeUserTextView.setText(userName);
        nv.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.home_menu:
                    Intent intent12 = new Intent(DashboardActivity.this, DashboardActivity.class);
                    startActivity(intent12);
                    break;
                case R.id.about_menu:
                    Intent intent = new Intent(DashboardActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout_menu:
                    Intent intent1 = new Intent(DashboardActivity.this, LoginActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    finish();
                    break;
                default:
                    return true;
            }


            return true;

        });

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddtasksActivity.class);
            startActivity(intent);
        });
    }

    private void doNetwork() {
        /*recyclerview*/
        APIEndpoints apiInterface = getClient().create(APIEndpoints.class);
        User.User_ user_=new User.User_();
        user_.setId(user_id);
        apiInterface.gettasksByUserId(user_).enqueue(new Callback<Tasks>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.isSuccessful()) {
                    // taskAdapters = new TaskAdapters(tasksList);
                    if (response.body() != null) {
                        List<Datum> tasksList;
                        tasksList = response.body().getData();
                        Collections.reverse(tasksList);
                        TaskAdapters adapters = new TaskAdapters(DashboardActivity.this, tasksList);
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = df.format(c);
//                        String formattedDate = df.format("Mon Jul 08 18:38:12 GMT+05:45 2019");

                        for (int i = 0; i < tasksList.size(); i++) {
                            /*TODO:date format*/
                            if (tasksList.get(i).getDueDate().equalsIgnoreCase(formattedDate)) {
                                showNotification("Reminder", tasksList.get(i).getTaskName());
                            }
                        }
                        recyclerView.setAdapter(adapters);
                        /*-----------------notification code start----------------------*/

//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        while (true) {
//                            recyclerView.setAdapter(adapters);
//                            for (Datum datum : tasksList) {
//                                String todayDate = dateFormat.format(System.currentTimeMillis());
//                                if (todayDate.equals(datum.getDueDate()) && !notifyDate.equals(todayDate)) {
//                                    showNotification("Reminder", datum.getTaskName());
//                                    notifyDate = todayDate;
//
//                                }
//                            }
//
//                        }

//                        showNotification();


                    }
                }

            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void showNotification(String notification_title, String setContent) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Notification notification = new Notification.Builder(DashboardActivity.this)
                    .setContentTitle(notification_title)
                    .setContentText(setContent)
                    .setSmallIcon(R.drawable.icon)
                    .setChannelId(CHANNEL_ID)
                    .build();
            notificationManager.notify(1, notification);
        }else {
            Notification notification = new Notification.Builder(DashboardActivity.this)
                    .setContentTitle(notification_title)
                    .setContentText(setContent)
                    .setSmallIcon(R.drawable.icon)
                    .build();
            notificationManager.notify(1, notification);
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        manager.registerListener(event, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.unregisterListener(event);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(String.valueOf(CHANNEL_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can'actionBarDrawerToggle change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
