package com.example.black.gotankdriver.service;

import com.example.black.gotankdriver.converter.WrappedListResponse;
import com.example.black.gotankdriver.converter.WrappedResponse;
import com.example.black.gotankdriver.model.PemesanModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoriService {
    @GET("driver/histori/{id}")
    Call<WrappedListResponse<PemesanModel>> showPemesan(@Path("id") String id);

    @GET("driver/detail/{id}")
    Call<WrappedResponse<PemesanModel>> detailPemesanHistori(@Path("id") String id);

    @FormUrlEncoded
    @POST("pesan/konfirmasi/{id}")
    Call<WrappedResponse<PemesanModel>> konfirmasiSelesai(@Path("id") String id, @Field("status") String status);


}
