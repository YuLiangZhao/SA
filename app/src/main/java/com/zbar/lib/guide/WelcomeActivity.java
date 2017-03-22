package com.zbar.lib.guide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

import com.zbar.lib.R;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.app_web.LoginActivity;

/**
 * android android第一屏和引导界面
 *
 * 作者
 *
 */
public class WelcomeActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;//开启之后停顿3秒

    @Override
    public void initWidget() {
        this.setAllowFullScreen(true);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_welcome);
        //保存数据到SharePreference中以此判断是不是第一次登陆
        SharedPreferences sp= getSharedPreferences("FirstLaunch",MODE_PRIVATE);
        sp.edit().putBoolean("FirstValue",false).commit();   //修改存储的boolean值

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }
}