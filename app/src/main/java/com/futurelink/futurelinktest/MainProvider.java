package com.futurelink.futurelinktest;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainProvider {
    private MainService service;
    private ProgressDialog loading;

    public MainProvider(Context context) {
        this.service = HttpClient.getInstance().create(MainService.class);
        this.loading = new ProgressDialog(context);
        this.loading.setMessage("Cargando...");
    }

    public void getStart(MainDelegate delegate) {
        MainProvider.this.loading.show();
        Call<ResponseBody> call = service.start();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                MainProvider.this.loading.dismiss();
                delegate.onSuccess(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                MainProvider.this.loading.dismiss();
                delegate.onError(t);
            }
        });
    }

    public void getActivate(String statefulHash, MainDelegate delegate) {
        MainProvider.this.loading.show();
        Call<ResponseBody> call = service.activate(SessionManager.getInstance().sessionId, statefulHash, "name");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                MainProvider.this.loading.dismiss();
                delegate.onSuccess(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                MainProvider.this.loading.dismiss();
                delegate.onError(t);
            }
        });
    }

    public void getPayload(String url, MainDelegate delegate) {
        MainProvider.this.loading.show();

        Call<ResponseBody> call = service.payload(url, SessionManager.getInstance().sessionId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                MainProvider.this.loading.dismiss();
                delegate.onSuccess(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                MainProvider.this.loading.dismiss();
                delegate.onError(t);
            }
        });
    }

    public void postReaper(String url, Map<String, String> request, MainDelegate delegate) {
        MainProvider.this.loading.show();

        Call<ResponseBody> call = service.reaper(url, SessionManager.getInstance().sessionId, request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                MainProvider.this.loading.dismiss();
                delegate.onSuccess(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                MainProvider.this.loading.dismiss();
                delegate.onError(t);
            }
        });
    }
}
