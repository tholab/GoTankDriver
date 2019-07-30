package com.example.black.gotankdriver.service;

import com.example.black.gotankdriver.converter.WrappedResponse;
import com.example.black.gotankdriver.model.Driver;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DriverService {

    @FormUrlEncoded
    @POST("driver/login")
    Call<WrappedResponse<Driver>> login(@Field("name") String name, @Field("password") String password);


    @GET("driver/{id}")
    Call<WrappedResponse<Driver>> showById(@Path("id") String id);

    @FormUrlEncoded
    @PUT("driver/{id}")
    Call<WrappedResponse<Driver>> updateProfile(@Path("id") String id, @Field("name") String name);

    @Multipart
    @POST("driver/image/{id}")
    Call<WrappedResponse<Driver>> updateImage(@Path("id") String id, @Part MultipartBody.Part image);

}
