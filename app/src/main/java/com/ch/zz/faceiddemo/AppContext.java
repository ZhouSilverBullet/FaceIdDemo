package com.ch.zz.faceiddemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by admin on 2017/11/25.
 */

public class AppContext extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
