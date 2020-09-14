package com.futurelink.futurelinktest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainProvider {
    private Context context;
    private MainService service;
    private ProgressDialog loading;

    public MainProvider(Context context) {
        this.context = context;
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

    public void postReaper(String url, MainDelegate delegate) {
        MainProvider.this.loading.show();

        HackingHepler.copyAssets(this.context);

        ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
        File directory = contextWrapper.getDir("resources", Context.MODE_PRIVATE);

        Log.d("Directory:", directory.getAbsolutePath());

        File image = new File(directory, "image.jpg");
        File resume = new File(directory, "resume.pdf");
        File code = new File(directory, "code.java");

        String email = "alfrecanodiaz@gmail.com";
        String name = "Alfredo Manuel Cano Díaz";
        String aboutMe = "Web And Mobile Developer";

        Map<String, RequestBody> request = new HashMap<>();
        request.put("image", RequestBody.create(image, MediaType.parse("image/jpeg")));
        request.put("resume", RequestBody.create(resume, MediaType.parse("application/pdf")));
        request.put("code", RequestBody.create(code, MediaType.parse("application/java")));
        request.put("email", RequestBody.create(email, MediaType.parse("text/plain")));
        request.put("name", RequestBody.create(name, MediaType.parse("text/plain")));
        request.put("aboutme", RequestBody.create(aboutMe, MediaType.parse("text/plain")));

        Call<ResponseBody> call = service.reaper(url, SessionManager.getInstance().sessionId, request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("Reaper response:", response.toString());
                MainProvider.this.loading.dismiss();
                delegate.onSuccess(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d("Reaper failure:", t.getMessage());
                MainProvider.this.loading.dismiss();
                delegate.onError(t);
            }
        });
    }
}
