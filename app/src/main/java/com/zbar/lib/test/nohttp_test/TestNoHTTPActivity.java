package com.zbar.lib.test.nohttp_test;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;

/**
 * Created by Administrator on 2017/2/28.
 */

public class TestNoHTTPActivity extends BaseActivity {
    private SharedPreferences sp;
    private ImageView ivFlash;
    private String gif_url = "http://tupian.qqjay.com/u/2013/1127/19_222949_11.jpg";
    //private String gif_url = "http://tupian.qqjay.com/u/2013/1127/19_222949_8.jpg";

    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_welcome);
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        //
        ivFlash = findViewById(R.id.app_Flash_imageView);
        ivFlash.setOnClickListener(this);
        //
        //requestUserInfo();
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.app_Flash_imageView:
                //String url = "http://1.gsedu.sinaapp.com/GsResVB/Head.php";
                //创建NoHttp的请求对象,设置加载的网络路径和请求
                Request<Bitmap> request = NoHttp.createImageRequest(gif_url, RequestMethod.GET);
                //获取核心的NoHttp网络工具类对象
                CallServer callServerInstance = CallServer.getRequestInstance();
                //把队列添加进去
                callServerInstance.add(this, 0, request, new HttpListener<Bitmap>() {
                    @Override
                    public void onSucceed(int what, Response response) {
                        //showMessage(response.get().toString());
                        ivFlash.setImageBitmap((Bitmap)response.get());
                    }

                    @Override
                    public void onFailed(int what, Response response) {
                        showMessage(response.get().toString());
                    }
                }, true, true);
                break;
        }
    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }


    /**
     * 请求用户信息。
     */
    private void requestUserInfo() {
        /*
        Request<String> req = NoHttp.createStringRequest("http://1.gsedu.sinaapp.com/GsResVB/Head.php", RequestMethod.GET);
        dialog= new WaitDialog(this);
        request(0, req, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                // 请求开始，可以显示一个dialog。
                dialog.setTitle("提示");
                dialog.setMessage("loading...");
                //showMessage("请求开始...");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                // 请求成功，这里判断服务器的响应码，假如你们服务器200时，才是业务成功：
                Headers headers = response.getHeaders();
                if(headers.getResponseCode() == 200) {
                    String result = response.get(); // 拿到结果。
                    //dialog.setProgress(100);
                    //dialog.dismiss();
                    showMessage("result:"+ result);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                // 请求失败，具体失败类型，请看nohttp主页的demo。\
                showMessage("response:"+ response);
            }

            @Override
            public void onFinish(int what) {
                // 请求结束，关闭dialog。
                dialog.dismiss();
                //showMessage("请求结束");
            }
        });


        Request<Bitmap> req2 = NoHttp.createImageRequest(gif_url, RequestMethod.GET);
        request(1, req2, new OnResponseListener<Bitmap>() {
            @Override
            public void onStart(int what) {
                showMessage("请求开始...");
            }

            @Override
            public void onSucceed(int what, Response<Bitmap> response) {
                if (what == 1) {
                    ivFlash.setImageBitmap(response.get());
                }
            }

            @Override
            public void onFailed(int what, Response<Bitmap> response) {
                //传下来的每个值都很容易从其名称看出来作用
                //networkMillis--请求消耗的时间
                showMessage("请求失败...");
            }

            @Override
            public void onFinish(int what) {
                showMessage("请求结束...");
            }
        });


        Request<JSONObject> req3 = NoHttp.createJsonObjectRequest("http://1.lzedu.sinaapp.com/SA/SA_RandStudentOne.php");
        request(2, req3, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                showMessage("请求开始...");
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                System.out.println(response.toString());
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                //传下来的每个值都很容易从其名称看出来作用
                //networkMillis--请求消耗的时间
                showMessage("请求失败...");
            }

            @Override
            public void onFinish(int what) {
                showMessage("请求结束...");
            }
        });
        */

    }
}
