package com.ch.zz.faceiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ch.zz.faceiddemo.http.FDRequest;
import com.ch.zz.faceiddemo.utils.T;

import java.util.HashMap;

public class RegistActivity extends AppCompatActivity {

    private EditText registAccount;
    private EditText registPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.fb_regist_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist();
            }
        });
    }

    private void regist() {
        String accountValue = registAccount.getText().toString().trim();
        String pwdValue = registPwd.getText().toString().trim();
        if (TextUtils.isEmpty(accountValue)) {
            T.show("帐号为空");
            return;
        }
        if (TextUtils.isEmpty(pwdValue)) {
            T.show("密码为空");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("un", accountValue);
        map.put("pd", pwdValue);
        FDRequest.post("http://face.zhouyang.space/user/register", map);
    }

    private void initView() {
        registAccount = findViewById(R.id.fd_regist_account_edit);
        registPwd = findViewById(R.id.fd_regist_pwd_edit);

    }
}
