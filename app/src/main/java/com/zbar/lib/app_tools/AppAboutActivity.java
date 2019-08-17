package com.zbar.lib.app_tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.app_web.WebViewActivity;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.util.AppUtil;
import com.zbar.lib.util.NetworkUtil;
import com.zbar.lib.util.StringUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2017/3/25.
 */

public class AppAboutActivity extends BaseActivity {
    private String TID;
    private SharedPreferences sp;
    private TextView tvTitle,tvAppName,tvInfo,tvNotice,tvIdea,tvUpdate;
    private ImageButton ibTopBack,ibAddBtn;
    private int oldVer = 0;
    private String apkUrl = "";
    private String apkName = "SA_NewVersion.apk"; //下载到本地要给这个APP命的名字
    private Handler m_mainHandler;
    private ProgressDialog m_progressDlg;

    @Override
    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_app_about);
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器

        ibTopBack = findViewById(R.id.ib_top_back);
        tvTitle = findViewById(R.id.tv_top_title);
        tvAppName = findViewById(R.id.tv_app_name);
        tvInfo = findViewById(R.id.tv_app_info);
        tvNotice = findViewById(R.id.tv_app_notice);
        tvIdea = findViewById(R.id.tv_app_idea);
        tvUpdate = findViewById(R.id.tv_app_update);

        ibTopBack.setOnClickListener(this);
        tvTitle.setText(R.string.app_about);
        tvAppName.setText(R.string.app_name);

        tvInfo.setOnClickListener(this);
        tvNotice.setOnClickListener(this);
        tvIdea.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);

        m_mainHandler = new Handler();
        m_progressDlg =  new ProgressDialog(this);
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        m_progressDlg.setIndeterminate(false);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_back:
                finish();
                break;
            case R.id.tv_app_info:
                //打开 功能介绍 网页
                IE("http://lzedu.sinaapp.com/SA/index.php");
                break;
            case R.id.tv_app_notice:
                //打开 系统通知 网页
                IE("http://lzedu.sinaapp.com/SA/index.php");
                break;
            case R.id.tv_app_idea:
                //打开 意见建议 网页
                IE("http://lzedu.sinaapp.com/SA/index.php");
                break;
            case R.id.tv_app_update:
                //showMessage("实现APP检查更新功能！！");
                CheckUpdate();
                break;
            default:
        }
    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }
    //APP内 打开指定网址
    private void IE(String url){
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
    //APP检查更新功能
    private void CheckUpdate(){
        oldVer = getAppVerCode();
        if(NetworkUtil.isOnLine(getApplicationContext())) {
            CheckAppUpdate();
        }else{
            showMessage("手机没网络，请检查WIFI或数据网络环境！");
            //finish();//返回上一层界面
        }
    }
    public int getAppVerCode(){
        return AppUtil.getAppVersionCode(this);
    }
    public String getAppVerName(){
        return AppUtil.getAppVersionName(this);
    }
    //APP检查更新功能
    private void CheckAppUpdate(){
        //String CheckUpdateUrl = "http://192.168.0.108/SA/SA_CheckAppUpdate.php";
        String CheckUpdateUrl = "http://lzedu.sinaapp.com/SA/SA_CheckAppUpdate.php";
        String tid = sp.getString("TcID","0");
        //ToastUtil.showToast(getApplicationContext(),"tid=" + tid);
        String TID = StringUtil.Base64Encode(tid);
        //ToastUtil.showToast(getApplicationContext(),"TID=" + TID);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(CheckUpdateUrl, RequestMethod.POST);
        request.add("TID", TID);
        request.add("verCode", oldVer);
        CallServer.getRequestInstance().add(this, 0, request, new HttpListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response response) {
                //showMessage(response.get().toString());
                JSONObject jsonObject = (JSONObject)response.get();
                Log.i("SA_CheckAppUpdateJSON：",response.toString());
                try {
                    if (jsonObject.has("Data")) {
                        JSONObject AppData = jsonObject.getJSONObject("Data");
                        int verCode = AppData.getInt("verCode");
                        if(verCode > oldVer){
                            //有新版本
                            String verName  = AppData.getString("verName");
                            String verRemark  = AppData.getString("verRemark");
                            apkUrl  = AppData.getString("verUrl");
                            //showMessage("有新版本！"+ verName + "\n" + verCode + "\n" + verRemark + "\n" + apkUrl );
                            TipsNew(verCode,verName,verRemark);
                            //Update();

                        }else{
                            //当前已经是最新版
                            //showMessage("当前已经是最新版！");
                            TipsOld();
                        }

                    }else{
                        String err = jsonObject.getString("err");
                        String msg = jsonObject.getString("msg");
                        showMessage(err + ":" + msg + "\n检查更新失败，请稍后再试！");
                    }
                } catch (Exception e) {
                    showMessage("检查更新失败，请稍后再试！"+e.toString());
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                showMessage("检查更新失败，请稍后再试！");
            }
        }, true, true);
    }
    /**
     * 提示更新新版本
     */
    private void TipsNew(int newVerCode,String newVerName,String newVerRemark) {
        int verCode = getAppVerCode();
        String verName = getAppVerName();

        String str= "当前版本："+verName+" ,新版本："+newVerName
                +"\nCode:"+verCode+" Code:"+newVerCode+
                "\n更新说明:"+newVerRemark+" \n是否更新？";
        Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新").setMessage(str)
                // 设置内容
                .setPositiveButton("更新",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                downFile(apkUrl, apkName);  //开始下载
                            }
                        })
                .setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // 点击"取消"按钮之后退出程序
                                //finish();
                                dialog.dismiss();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }
    /**
     *  提示当前为最新版本
     */
    private void TipsOld()
    {
        int verCode = getAppVerCode();
        String verName = getAppVerName();
        String str="当前版本:"+verName+" Code:"+verCode+",\n已是最新版,无需更新!";
        Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新")
                .setMessage(str)// 设置内容
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                //finish();
                                dialog.dismiss();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }
    private void downFile(String url,String filename){
        String fileFolder = Environment.getExternalStorageDirectory().toString();
        //下载文件
        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url,RequestMethod.GET,fileFolder,filename,false,true);
        // what 区分下载
        // downloadRequest 下载请求对象
        // downloadListener 下载监听
        CallServer.getDownloadInstance().add(0, downloadRequest,  new DownloadListener() {

            @Override
            public void onDownloadError(int what, Exception exception) {
                // 下载发生错误
                showMessage("下载发生错误");
                m_progressDlg.cancel();
            }

            @Override
            public void onStart(int what, boolean resume, long preLenght, Headers header, long count) {
                // 下载开始
                //showMessage("下载开始");
                m_progressDlg.show();
            }

            @Override
            public void onProgress(int what, int progress, long downCount, long speed) {
                // 更新下载进度和下载网速
                //showMessage("更新下载进度和下载网速");
                m_progressDlg.setTitle("程序更新");
                m_progressDlg.setMessage("正在下载...");
                m_progressDlg.setProgress(progress);
            }

            @Override
            public void onFinish(int what, String filePath) {
                // 下载完成
                //showMessage("下载完成");
                m_progressDlg.cancel();
                update();
            }

            @Override
            public void onCancel(int what) {
                // 下载被取消或者暂停
                //showMessage("下载被取消或者暂停");
                m_progressDlg.cancel();
            }
        });
    }

    /**
     * 安装程序
     */
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), apkName)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
