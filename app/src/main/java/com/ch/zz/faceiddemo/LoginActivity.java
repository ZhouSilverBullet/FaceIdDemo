package com.ch.zz.faceiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ch.zz.faceiddemo.bean.UserBean;
import com.ch.zz.faceiddemo.http.FDRequest;
import com.ch.zz.faceiddemo.http.HttpCallback;
import com.ch.zz.faceiddemo.utils.FDLocation;
import com.ch.zz.faceiddemo.utils.T;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText accountText;
    private EditText pwdText;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initView() {
        accountText = findViewById(R.id.fd_login_account_edit);
        pwdText = findViewById(R.id.fd_login_pwd_edit);
        submit = findViewById(R.id.fb_login_submit);
    }

    private void initEvent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String accountValue = accountText.getText().toString().trim();
        String pwdValue = pwdText.getText().toString().trim();
        if (TextUtils.isEmpty(accountValue)) {
            T.show("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwdValue)) {
            T.show("密码不能为空");
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("un", accountValue);
        map.put("pd", pwdValue);
        double latitude = FDLocation.getLatitude();
        double longitude = FDLocation.getLongitude();
        if (latitude == 0 && longitude == 0) {
            FDLocation.getInstance().location();
        }
        latitude = FDLocation.getLatitude();
        longitude = FDLocation.getLongitude();
        map.put("jd", latitude + "");
        map.put("wd", longitude + "");

        FDRequest.post("http://face.zhouyang.space/user/login", map, new HttpCallback<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> map) {
                if (map != null) {
                    UserBean userBean = new UserBean();
                    userBean.ui = map.get("ui");
                    userBean.un = map.get("un");
                    userBean.pd = map.get("pd");
                    userBean.jd = map.get("jd");
                    userBean.wd = map.get("wd");
                    Gloab.getInstance().setBean(userBean);
                }
                T.show("登陆成功");
                //网络登录
                setResult(99);
                finish();
            }

            @Override
            public void onFailure(int code, String error) {
                T.show(error);
            }
        });

    }
}
