package com.example.daam.api;

import com.example.daam.model.LoginRequest;
import com.example.daam.model.LoginResponse;
import com.example.daam.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EnergyPlatformApi {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("tasks/worker/{email}")
    Call<List<Task>> getWorkerTasks(@Path("email") String email, @Header("Authorization") String token);

    @GET("products")
    Call<List<com.example.daam.model.Product>> getProducts(@Header("Authorization") String token);

    @PUT("tasks/{id}/status")
    Call<Task> updateTaskStatus(
            @Path("id") Long id,
            @Body com.example.daam.model.UpdateTaskStatusRequest request,
            @Header("Authorization") String token);
}
