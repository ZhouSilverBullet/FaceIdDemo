package com.ch.zz.faceiddemo;

import com.ch.zz.faceiddemo.bean.UserBean;

/**
 * Created by zhousaito on 2017/11/27.
 */

public class Gloab {
    private static Gloab gloab;

    private Gloab() {

    }

    public static Gloab getInstance() {
        if (gloab == null) {
            gloab = new Gloab();
        }
        return gloab;
    }

    private UserBean bean;

    public UserBean getBean() {
        return bean;
    }

    public void setBean(UserBean bean) {
        this.bean = bean;
    }
}
