package com.futurelink.futurelinktest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionManager {
    private static SessionManager manager;

    String sessionId;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (manager == null) {
            manager = new SessionManager();
        }

        return manager;
    }
}
