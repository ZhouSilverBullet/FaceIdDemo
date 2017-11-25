package com.ch.zz.faceiddemo.http;

import android.os.Handler;
import android.os.Looper;

import com.ch.zz.faceiddemo.utils.T;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2017/11/25.
 */

public class FDRequest {
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Handler handler = new Handler(Looper.getMainLooper());


    public static void post(String url, String imgBase) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("img", imgBase);
        MultipartBody build = builder.build();
        Request request = new Request.Builder().post(build).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        T.show("失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        T.show("成功");
                    }
                });
            }
        });
    }

    public static void post(String url, HashMap<String, String> map) {
        FormBody build = new FormBody.Builder().add("un", map.get("un")).
                add("pd", map.get("pd")).
                add("jd", "111").add("wd", "111").build();
        Request request = new Request.Builder().post(build).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        T.show("失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        T.show("成功");
                    }
                });
            }
        });
    }
}
