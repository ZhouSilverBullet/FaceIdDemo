package com.ch.zz.faceiddemo.utils;

import android.widget.Toast;

import com.ch.zz.faceiddemo.AppContext;

/**
 * Created by admin on 2017/11/25.
 */

public class T {

    private static Toast toast;

    public static void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(AppContext.getContext(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
