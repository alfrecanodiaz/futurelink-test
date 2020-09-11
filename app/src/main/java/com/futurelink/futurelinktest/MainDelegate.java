package com.futurelink.futurelinktest;

import okhttp3.ResponseBody;
import retrofit2.Response;

public interface MainDelegate {
    void onSuccess(Response<ResponseBody> response);
    void onError(Throwable error);
}
