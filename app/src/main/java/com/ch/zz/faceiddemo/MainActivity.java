package com.ch.zz.faceiddemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ch.zz.faceiddemo.utils.FDLocation;
import com.ch.zz.faceiddemo.utils.T;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private TextView statusText;
    private Button loginSkip;
    public static final int REQUEST_CODE = 100;
    private View mainLogin;
    private View regist;
    private View cameraLoginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
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

    @Override
    protected void onResume() {
        super.onResume();
        handleBtn();
    }

    private void handleBtn() {
        Gloab instance = Gloab.getInstance();
        if (instance.getBean() == null || TextUtils.isEmpty(instance.getBean().ui)) {
            cameraLoginView.setVisibility(View.GONE);
            mainLogin.setVisibility(View.GONE);
        } else {
            cameraLoginView.setVisibility(View.VISIBLE);
            mainLogin.setVisibility(View.VISIBLE);
        }
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

        regist = findViewById(R.id.fd_login_registration);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

        cameraLoginView = findViewById(R.id.fd_rigit_to_main);
        cameraLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraLogin();
            }
        });
    }

    private void cameraLogin() {
        Intent intent = new Intent(this, FaceDetectExpActivity.class);
        intent.putExtra("type", 10);
        startActivityForResult(intent, REQUEST_CODE_NO_ACTION_DETECT);
    }

    private static final int REQUEST_CODE_PICK_IMAGE = 1000;
    private static final int REQUEST_CODE_NO_ACTION_DETECT = 100;
    private static final int REQUEST_CODE_LIVENESS_DETECT = 101;

    private void registration() {
        Intent intent = new Intent(this, LRActivity.class);
        intent.putExtra("type", 10);
        startActivityForResult(intent, 13);
    }

    private void manLogin() {
        Intent intent = new Intent(this, FaceDetectExpActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NO_ACTION_DETECT);
    }

    private void skipLogin() {
        Intent intent = new Intent(this, LRActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            statusText.setText("状态：登录成功！");
            handleBtn();
        } else if (requestCode == 13) {
            statusText.setText("状态：注册成功！");
        } else if (requestCode == 14) {
            statusText.setText("状态：绑定成功！");
        } else if (requestCode == 15) {
            statusText.setText("状态：人脸验证成功！");
        }
    }

    private void initView() {
        statusText = (TextView) findViewById(R.id.fd_text_status);
        loginSkip = (Button) findViewById(R.id.fd_login_skip);
        mainLogin = findViewById(R.id.fd_login_to_main);
    }
}
