package com.zbar.lib;

import android.app.Application;

import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by Administrator on 2017/3/1.
 */

public class BaseApplication extends Application {
    public static final String SAVE_PATH = "SA_Update";//APP更新下载目录
    public static RequestQueue WebQueue = null;//NoHttp全局变量
    @Override
    public void onCreate() {
        super.onCreate();
        //NoHttp默认初始化
        NoHttp.initialize(this, new NoHttp.Config()
                .setConnectTimeout(30 * 1000) // 全局连接超时时间，单位毫秒。
                .setReadTimeout(30 * 1000) // 全局服务器响应超时时间，单位毫秒。);
                .setCacheStore(
                        new DBCacheStore(this) // 配置缓存到数据库。
                                .setEnable(true) // true启用缓存，fasle禁用缓存。
                )
                .setCookieStore(
                        new DBCookieStore(this)
                                .setEnable(false) // true启用自动维护Cookie，fasle禁用自动维护Cookie。
                )
                // 使用HttpURLConnection
                //.setNetworkExecutor(new URLConnectionNetworkExecutor())
                // 或者使用OkHttp
                // .setNetworkExecutor(new OkHttpNetworkExecutor())
        );
        //打开调试模式、设置TAG
        Logger.setDebug(true); // 开启NoHttp调试模式。
        Logger.setTag("NoHttp"); // 设置NoHttp打印Log的TAG。
        WebQueue = NoHttp.newRequestQueue();
    }
}
