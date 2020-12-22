package com.dangqx.bookkeeping.application;

import android.app.Application;
import android.content.Context;


import com.xuexiang.xui.XUI;

import org.litepal.LitePal;


/**
 * Created by dang on 2020-12-07.
 * Time will tell.
 *
 * @description
 */
public class myApplication extends Application {

    private static Context mContext;
    //onCreate--->程序创建的时候执行
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        mContext = getApplicationContext();
        //Log.e("MyApplication", "MyApplication----onCreate()方法！！！！！！！！！！！！");

        //引入litepal
        LitePal.initialize(this);
    }
}
