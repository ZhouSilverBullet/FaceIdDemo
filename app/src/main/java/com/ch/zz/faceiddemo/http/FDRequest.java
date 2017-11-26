package com.ch.zz.faceiddemo.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ch.zz.faceiddemo.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    public static void post(String url, HashMap<String, String> map, final HttpCallback<HashMap<String, String>> callback) {
        FormBody build = new FormBody.Builder().add("un", map.get("un")).
                add("pd", map.get("pd")).
                add("jd", map.get("jd")).add("wd", map.get("wd")).build();
        Request request = new Request.Builder().post(build).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(0, "");
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(result)) {
                            if (callback != null) {
                                callback.onFailure(0, "未知错误(111)");
                            }
                            return;
                        }

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                        } catch (JSONException e) {
                            if (callback != null) {
                                callback.onFailure(0, "");
                            }
                        }

                        if (jsonObject != null && callback != null) {
                            int code = JsonUtil.getStatusCode(jsonObject);
                            String msg = JsonUtil.getServerMsg(jsonObject);
                            if (code != 200) {
                                callback.onFailure(code, msg);
                            } else {
                                Object data = JsonUtil.getJsonData(jsonObject);
                                if (data instanceof JSONObject) {
                                    callback.onSuccess(JsonUtil.getMapFromJson((JSONObject) data));
                                } else if (data instanceof JSONArray) {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("data", data.toString());
                                    callback.onSuccess(map);
                                } else {
                                    callback.onSuccess(null);
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}
