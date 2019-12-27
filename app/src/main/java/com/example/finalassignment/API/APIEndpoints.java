package com.example.finalassignment.API;

import com.example.finalassignment.models.Datum;
import com.example.finalassignment.models.DeleteResponse;
import com.example.finalassignment.models.RegisterResponse;
import com.example.finalassignment.models.User;
import com.example.finalassignment.models.Tasks;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIEndpoints {
    @POST("users/authenticate")
    Call<User> login(@Body  User.User_ user);


    @POST("users/register")
    Call<RegisterResponse> signUp(@Body User.User_ user);

    @POST("tasks")
    Call<Tasks> addTask(@Body Datum datum);


    @GET("tasks")
    Call<Tasks>getAllTasks();

    @POST("tasksByUserId")
    Call<Tasks>gettasksByUserId(@Body  User.User_ user);

    @DELETE("tasks/{task_id}")
    Call<DeleteResponse>deleteTask(@Path("task_id")  String taskId);

    @PUT("tasks/{task_id}")
    Call<ResponseBody>updateTask(@Path("task_id")  String taskId , @Body Datum datum);
}
