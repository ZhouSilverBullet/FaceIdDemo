package com.ch.zz.faceiddemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ch.zz.faceiddemo.utils.FDLocation;
import com.ch.zz.faceiddemo.utils.T;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private TextView statusText;
    private Button loginSkip;
    public static final int REQUEST_CODE = 100;
    private View mainLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            location();
        } else {
            EasyPermissions.requestPermissions(this, "请开启手机定位再使用", 100, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 100) {
            T.show("定位权限已经打开");
            FDLocation.getInstance().location();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 100) {
            T.show("请开启权限在打开app");
            finish();
        }
    }

    private void location() {
        FDLocation.getInstance().location();
    }


    private void initEvent() {
        loginSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipLogin();
//                new Thread(){
//                    @Override
//                    public void run() {
//                        int test_con = R.drawable.test_con;
//                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), test_con);
//                        FDRequest.post("", Base64PicUtil.bitmapToString(bitmap));
//                    }
//                }.start();
            }
        });
        mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manLogin();
            }
        });

        findViewById(R.id.fd_login_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

        findViewById(R.id.fd_rigit_to_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraLogin();
            }
        });
    }

    private void cameraLogin() {
        Intent intent = new Intent(this, CameraLoginActivity.class);
        intent.putExtra("type", 10);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void registration() {
        Intent intent = new Intent(this, RegistActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void manLogin() {
        Intent intent = new Intent(this, CameraLoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void skipLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            statusText.setText("登录状态：登录成功！");
        }
    }

    private void initView() {
        statusText = findViewById(R.id.fd_text_status);
        loginSkip = findViewById(R.id.fd_login_skip);
        mainLogin = findViewById(R.id.fd_login_to_main);
    }
}
