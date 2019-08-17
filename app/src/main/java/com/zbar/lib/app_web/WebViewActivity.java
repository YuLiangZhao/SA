package com.zbar.lib.app_web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zbar.lib.R;

public class WebViewActivity extends Activity {
    private String TID;

    private ImageButton ibTopBack,ibTopClose;
    private TextView tvTopTitle;
    private WebView webView;
    private Handler mHandler = new Handler();
    private String URL;
    private Boolean Auto;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_webview);
        SharedPreferences sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        //获取用户名、密码、
        TID = sp.getString("TcID","");
        URL = getIntent().getStringExtra("url");
        Auto  = getIntent().getBooleanExtra("auto",false);

        ibTopBack = findViewById(R.id.ib_top_back);
        tvTopTitle = findViewById(R.id.tv_top_title);
        ibTopClose = findViewById(R.id.ib_top_close);
        webView = findViewById(R.id.webView_Main);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许执行JavaScript
        webSettings.setDomStorageEnabled(true);//打不开网页解决方案
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);//打开页面时， 自适应屏幕
        //webSettings.setBuiltInZoomControls(false);//使页面支持缩放，显示放大缩小按钮
        webSettings.setSupportZoom(true);//使页面支持缩放
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //优先使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        webSettings.setAllowFileAccess(true);  //设置可以访问文件


        if(URL.indexOf("weixin.qq.com")> 0){
            URL = "http://weixin.qq.com/";//后期可尝试开启微信....
        }else {
            if(URL.indexOf("sinaapp.com")> 0) {
                //添加客户端标示
                String ua = webSettings.getUserAgentString();
                webSettings.setUserAgentString(ua + "; SAS_Phone_Client /");
                if (URL.indexOf("?") > 0) {
                    URL = URL + "&TID="+TID;//教师二维码登录附加参数
                } else {
                    URL = URL + "?TID="+TID;//教师二维码登录附加参数
                }
            }
        }
        //打开网页
        webView.loadUrl(URL);
        //String str = "圆明园";
        //String url = "javascript:alert('" + str + "')";//调用网页Js
        //webView.loadUrl(url);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        //判断页面加载过程
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    if(Auto){
                        new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                finish();//如果开启了连续扫描，则，加载完成就会自动返回 二维码扫描界面。
                            }
                        }.start();
                    }
                } else {
                    // 加载中
                }
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //getWindow().setTitle(title);
                //title = title + "使页面支持缩放，显示放大缩小按钮";
                tvTopTitle.setText(title);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //顶部 后退 按钮点击事件
        ibTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack(); //goBack()表示返回WebView的上一页面
                }else{
                    finish();//不能倒退就退出
                }
            }
        });
        //顶部 关闭 按钮点击事件
        ibTopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//直接关闭网页退出
            }
        });
    }

    // 这是他定义由 addJavascriptInterface 提供的一个Object
    final class MyJsInterface {
        MyJsInterface() {
        }
        /**
         * This is not called on the UI thread. Post more_pic_2_gif_loading runnable to invoke
         * 这不是呼吁界面线程。发表一个运行调用
         * loadUrl on the UI thread.
         * loadUrl在UI线程。
         */
        public void clickOnAndroid() {        // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
            mHandler.post(new Runnable() {
                public void run() {
                    // 此处调用 HTML 中的javaScript 函数
                    webView.loadUrl("javascript:wave()");
                }
            });
        }
        //AppDo 名下的Js操控函数
        /*js调用方法
        *<script type="text/javascript">
        *    function Close() {
        *       window.AppDo.GotoScan();
        *   }
        *</script>
        * */
        public void GotoScan(){
            getWindow().setTitle("返回到扫描界面！");
            //Intent intent = new Intent(this,WebViewActivity.class);
            //startActivity(intent);
            //finish();//返回到扫描界面
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WebViewActivity Page") // TODO: Define more_pic_2_gif_loading title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        webView.onPause();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //webView.stopLoading();
        webView.destroy();
        super.onDestroy();
    }
    @Override
    //按返回键时，不退出程序而是返回上一浏览页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
