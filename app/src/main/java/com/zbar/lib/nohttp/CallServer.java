package com.zbar.lib.nohttp;

import android.content.Context;
import android.os.Environment;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/1.
 */

public class CallServer {
    private static CallServer callServer;
    /**
     * 请求队列
     */
    private static RequestQueue requestQueue;
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
        downloadQueue = NoHttp.newDownloadQueue();
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

    /**
     * @param url      请求服务器链接
     * @param what     请求服务标识
     * @param map      请求参数封装
     * @param filePath 下载文件的保存路径与文件名
     * @param listener 下载监听
     */
    public void asyncDownloadFile(String url, int what,
                                     Map<String, String> map,
                                     String filePath, DownloadListener listener) {

        /**
         * url 下载服务器地址
         * RequestMethod.POST 发送请求方式
         * Environment.getExternalStorageDirectory().getPath() 文件下载保存路径
         * filePath 文件下载保存名称
         * true 支持断点下载
         * false 不覆盖已有的相同文件名称的文件
         */
        DownloadRequest downloadRequest
                = NoHttp.createDownloadRequest(url,
                RequestMethod.POST,
                Environment.getExternalStorageDirectory().getPath(),
                filePath, true, false);

        /**
         * 添加请求参数
         */
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    downloadRequest.add(entry.getKey(), entry.getValue());
                }
            }
        }
        /**
         * 设置请求标识
         */
        downloadRequest.setCancelSign(what);
        /**
         * 下载
         */
        downloadQueue.add(what, downloadRequest, listener);
    }
    /**
     * 分块级上传文件 提交表单数据
     * @param url
     * @param what      网络请求标识
     * @param forMap    网络请求提交表单数据
     * @param fileMap   网络请求提交上传文件
     * @param listener  网络请求标识
     */
    public void asyncUploadFile(String url, int what, Map<String, String> forMap,Map<String, String> fileMap, OnResponseListener<String> listener) {
        requestQueue.cancelBySign(what);
        Request<String> stringRequest = NoHttp.createStringRequest(url, RequestMethod.POST);
        /**
         * 添加上传文件信息
         */
        if (fileMap != null && !fileMap.isEmpty()) {
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                if (entry.getKey() != null) {
                    File file = new File(entry.getValue());
                    String fileName = file.getName();
                    stringRequest.addHeader("Content-Disposition", "form-data;name=\"" + fileName + "\";filename=\"" + fileName + "\"");
                    stringRequest.add(entry.getKey(), file);
                }
            }
        }
        /**
         * 添加请求参数
         */
        if (forMap != null && !forMap.isEmpty()) {
            for (Map.Entry<String, String> entry : forMap.entrySet()) {
                if (entry.getKey() != null) {
                    stringRequest.add(entry.getKey(), entry.getValue());
                }
            }
        }

        /**
         * 网络取消标识
         */

        stringRequest.setCancelSign(what);
        /**
         * 发起请求
         */
        requestQueue.add(what,stringRequest,listener);

    }
}
