package com.futurelink.futurelinktest;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MainService {
    @GET("start")
    Call<ResponseBody> start();

    @GET("activate")
    Call<ResponseBody> activate(@Header("Cookie") String sessionId, @Query("statefulhash") String statefulHash, @Query("username") String name);

    @GET
    Call<ResponseBody> payload(@Url String url, @Header("Cookie") String sessionId);

    @POST
    @Multipart
    Call<ResponseBody> reaper(@Url String url, @Header("Cookie") String sessionId, @PartMap Map<String, String> fields);
}
