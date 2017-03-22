package com.zbar.lib.nohttp;

import android.content.Context;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by Administrator on 2017/3/1.
 */

public class CallServer {
    private static CallServer callServer;
    /**
     * 请求队列
     */
    private RequestQueue requestQueue;
    /**
     * 下载队列
     */
    private static DownloadQueue downloadQueue;
    /**
     * 通过类名调用,获取CallServer对象
     */
    public synchronized static CallServer getRequestInstance() {
        if (callServer == null) {
            callServer = new CallServer();
        }
        return callServer;
    }
    //
    private CallServer() {
        requestQueue = NoHttp.newRequestQueue();
    }

    //添加一个请求到请求队列

    /**
     *
     * @param context   上下文,用来实例化自定义的对话框
     * @param what      标识,用来标志请求,当多个请求使用同一个时,在回调方法中
     * @param callback  网络请求的监听器
     * @param request   请求对象
     * @param canCancle 是否允许用户请求取消
     * @param isLoading 是否显示进度条对话框
     * @param <T>

     */
    public <T> void add(Context context, int what, Request<T> request, HttpListener<T> callback, boolean canCancle, boolean isLoading){
        requestQueue.add(what,request,new HttpResponseListener<T>(context,request,callback,canCancle,isLoading));
    }
    /**
     * 发起一个请求。
     *
     * @param what     what.
     * @param request  请求对象。
     * @param listener 结果监听。
     * @param <T>      要请求到的数据类型。
     */
    public <T> void add(int what, Request<T> request, OnResponseListener<T> listener) {
        requestQueue.add(what, request, listener);
    }
    /**
     * 下载队列
     */
    public static DownloadQueue getDownloadInstance() {
        if (downloadQueue == null)
            downloadQueue = NoHttp.newDownloadQueue();
        return downloadQueue;
    }
    /**
     * 取消这个sign标记的所有请求
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求
     */
    public void cancelAll() {
        requestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求
     */
    public void stopAll() {
        requestQueue.stop();
    }
}
