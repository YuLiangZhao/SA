package com.zbar.lib.test.nohttp_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;

/**
 * Created by Administrator on 2017/3/6.
 */

public class NoHTTPActivity extends Activity implements View.OnClickListener {
    private ImageView ivBG;
    private TextView mTvResult;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_welcome);

        ivBG = (ImageView) findViewById(R.id.app_Flash_imageView);
        mTvResult = (TextView) findViewById(R.id.app_author_textView);
        mTvResult.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_author_textView: // 完成编辑
                Toast.makeText(this,"app_author_textView Click",Toast.LENGTH_SHORT).show();
                getBaiDu();
                break;
            default:
                break;
        }
    }
    /**
     * 解析响应
     */
    @SuppressLint("SetTextI18n")
    private void response(Response<Bitmap> response) {
        if (response.isSucceed()) {
            //mTvResult.setText("请求成功: " + response.get());
            Bitmap bitmap = (Bitmap) response.get();
            ivBG.setImageBitmap(bitmap);
        } else {
            mTvResult.setText("请求失败: " + response.responseCode());
        }
    }
    /**
     * handler接受子线程结果
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {// 如果是同步请求发回来的结果
                @SuppressWarnings("unchecked")
                Response<Bitmap> response = (Response<Bitmap>) msg.obj;
                response(response);
            }
        }
    };
    private void  getBaiDu(){
        new Thread() {
            public void run() {
                // 在子线程中可以使用同步请求
                //Request<String> request = NoHttp.createStringRequest("http://www.baidu.com", RequestMethod.POST);
                //Response<String> response = NoHttp.startRequestSync(request);
                Request<Bitmap> request = NoHttp.createImageRequest("http://00.minipic.eastday.com/20170306/20170306183727_fd87b192d8cafca881924b8b8ed046da_1_mwpm_03200403.jpeg");
                Response<Bitmap> response = NoHttp.startRequestSync(request);

                handler.obtainMessage(0, response).sendToTarget();
            }
        }.start();
    }

}
