package com.ch.zz.faceiddemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ch.zz.faceiddemo.http.FDRequest;
import com.ch.zz.faceiddemo.utils.Base64PicUtil;

public class MainActivity extends AppCompatActivity {

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
    }

    private void registration() {
        Intent intent = new Intent(this, RegistActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void manLogin() {
        Intent intent = new Intent(this, CameraAct.class);
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
