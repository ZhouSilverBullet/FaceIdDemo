package com.ch.zz.faceiddemo.http;

/**
 * Created by zhousaito on 2017/11/27.
 */

public interface HttpCallback<T> {
    void onSuccess(T t);

    void onFailure(int code, String error);
}
