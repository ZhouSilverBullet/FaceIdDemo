package com.ch.zz.faceiddemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ch.zz.faceiddemo.R;
import com.ch.zz.faceiddemo.http.FDRequest;
import com.ch.zz.faceiddemo.http.HttpCallback;
import com.ch.zz.faceiddemo.utils.FDLocation;
import com.ch.zz.faceiddemo.utils.T;

import java.util.HashMap;

/**
 * Created by admin on 2017/12/6.
 */

public class RegisterFragment extends BaseFragment {
    private EditText registerAccount;
    private EditText registerPwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_register, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerAccount = (EditText) view.findViewById(R.id.fd_regist_account_edit);
        registerPwd = (EditText) view.findViewById(R.id.fd_regist_pwd_edit);

        view.findViewById(R.id.fb_regist_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String accountValue = registerAccount.getText().toString().trim();
        String pwdValue = registerPwd.getText().toString().trim();
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
        double latitude = FDLocation.getLatitude();
        double longitude = FDLocation.getLongitude();
        if (latitude == 0 && longitude == 0) {
            FDLocation.getInstance().location();
        }
        latitude = FDLocation.getLatitude();
        longitude = FDLocation.getLongitude();
        map.put("jd", latitude + "");
        map.put("wd", longitude + "");
        FDRequest.post("http://face.zhouyang.space/user/register", map, new HttpCallback<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> s) {
                T.show("注册成功");
                getActivity().setResult(13);
                getActivity().finish();
            }

            @Override
            public void onFailure(int code, String error) {
                T.show(error);
            }
        });
    }
}
