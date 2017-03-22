package com.zbar.lib.app_web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zbar.lib.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckHomeWorkActivity extends Activity {
    private String TID;

    private ImageButton ibTopBack,ibTopSearch,ibTopClose;
    private TextView tvTopTitle;
    private WebView webView;
    private String baseURL = "http://lzedu.sinaapp.com/SA/Student_HomeWork_Checker.php",URL;
    private TimePickerView pvTime,pvCustomTime;
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
        setContentView(R.layout.layout_activity_homework_checker);
        SharedPreferences sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        //获取用户名、密码、
        TID = sp.getString("TcID","");

        ibTopBack = (ImageButton)  findViewById(R.id.ib_top_back);
        tvTopTitle = (TextView)  findViewById(R.id.tv_top_title);
        ibTopClose = (ImageButton)  findViewById(R.id.ib_top_close);
        webView = (WebView) findViewById(R.id.webView_Main);
        initCustomTimePicker();//初始化日期选择器
        ibTopSearch = (ImageButton)  findViewById(R.id.ib_top_search);

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


        //添加客户端标示
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + "; SAS_Phone_Client /");
        if (baseURL.indexOf("?") > 0) {
            URL = baseURL + "&TID="+TID;//教师二维码登录附加参数
        } else {
            URL = baseURL + "?TID="+TID;//教师二维码登录附加参数
        }


        //打开网页
        webView.loadUrl(URL);

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
                    EndMe();
                    finish();//不能倒退就退出
                }
            }
        });
        //顶部 关闭 按钮点击事件
        ibTopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndMe();
                finish();//直接关闭网页退出
            }
        });
        //底部 日期选择 按钮点击事件
        ibTopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //URL = baseURL + "?d=123";
                //ToastUtil.showToast(getApplicationContext(),URL);
                //打开日期选择器，返回日期值后，自动打开日期相应的检查结果
                if (pvCustomTime != null) {
                    pvCustomTime.show(); //弹出自定义时间选择器
                }
            }
        });
    }
    //日期选择
    private void initCustomTimePicker() {
        // 注意，自定义布局中，optionspicker 或者 timepicker 的布局必须要有（即WheelView内容部分）
        // 否则会报空指针
        // 具体可参考demo 里面的两个自定义布局
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int day = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(year - 10,0,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(year + 5,month,day);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                //tvTopTitle.setText(getTime(date));
                URL = baseURL + "?d="+ getTime(date);
                //打开网页
                webView.loadUrl(URL);
            }
        })      .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate,endDate)
                /*
                .setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)
                */
                .setLayoutRes(R.layout.layout_activity_pickerview_date, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final Button btSubmit = (Button) v.findViewById(R.id.btnSubmit);
                        Button btCancel = (Button) v.findViewById(R.id.btnCancel);
                        btSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData(btSubmit);
                            }
                        });
                        btCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    private void EndMe(){
        if (pvCustomTime.isShowing()) {
            pvCustomTime.dismiss();
        }
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WebViewActivity Page") // TODO: Define a title for the content shown.
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
        EndMe();
        webView.stopLoading();
        webView.destroy();
        super.onDestroy();
    }
    @Override
    //按返回键时，不退出程序而是返回上一浏览页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EndMe();
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
