package com.zbar.lib.nohttp;

import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Administrator on 2017/3/1.
 */


public interface HttpListener<T> {
    //请求网络成功回调的方法
    void onSucceed(int what, Response<T> response);
    //请求网络失败回调的监听方法
    void onFailed(int what, Response<T> response);
}
