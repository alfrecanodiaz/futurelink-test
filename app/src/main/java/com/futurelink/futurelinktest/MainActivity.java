package com.futurelink.futurelinktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private MainProvider provider;
    private WebView webView;
    private Button buttonAction;

    private final int storageRequestCode = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.provider = new MainProvider(this);

        this.webView = findViewById(R.id.webView);
        buttonAction = findViewById(R.id.buttonAction);

        buttonAction.setOnClickListener(v -> {
            int readPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions();
            } else {
                MainActivity.this.proveYourSession();
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, this.storageRequestCode);
    }

    private void proveYourSession() {
        this.buttonAction.setVisibility(View.GONE);
        this.webView.setVisibility(View.VISIBLE);

        this.provider.getStart(new MainDelegate() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    MainActivity.this.getSession(response.headers());
                    MainActivity.this.displayHtmlContent(response.body());
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    private void getSession(Headers headers) {
        String setCookie = headers.get("Set-Cookie");

        String phpSESSID = setCookie.split(";")[0];
        SessionManager.getInstance().sessionId = phpSESSID;

        Log.d("Set-Cookie", setCookie);
        Log.d("PHPSESSID", phpSESSID);
    }

    private void displayHtmlContent(ResponseBody body) {
        try {
            Document document = Jsoup.parse(body.string());

            String html = document.html().replace("hidden", "text");

            this.webView.loadData(html, "text/html; charset=utf-8", "UTF-8");

            // stateful hash
            Element inputStatefulHash = document.select("input[name=statefulhash]").first();
            String statefulHash = inputStatefulHash.attr("value");

            Log.d("statefulhash", statefulHash);

            this.proveYouCanActivate(statefulHash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void proveYouCanActivate(String statefulHash) {
        this.provider.getActivate(statefulHash, new MainDelegate() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                Headers headers = response.headers();

                String payloadUrl = headers.get("X-Payload-URL");

                Log.d("X-Payload-URL:", payloadUrl);

                MainActivity.this.doThePayload(payloadUrl);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    private void doThePayload(String payloadUrl) {
        this.provider.getPayload(payloadUrl, new MainDelegate() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                Headers headers = response.headers();
                MainActivity.this.getSession(headers);

                String postBackUrl = headers.get("X-Post-Back-To");

                Log.d("X-Post-Back-To:", postBackUrl);

                MainActivity.this.upTheReaper(postBackUrl);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    private void upTheReaper(String reaperUrl) {
        this.provider.postReaper(reaperUrl, new MainDelegate() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {

            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasReadPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == this.storageRequestCode && hasReadPermission && hasWritePermission) {
            this.proveYourSession();
        }
    }
}