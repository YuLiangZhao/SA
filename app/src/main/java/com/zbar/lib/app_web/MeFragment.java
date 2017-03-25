package com.zbar.lib.app_web;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;
import com.zbar.lib.app_tools.AppAboutActivity;
import com.zbar.lib.custom_views.imgview.UserFaceImageView;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.util.ToastUtil;


/**
 * Created by Administrator on 2017/2/15.
 */

public class MeFragment extends Fragment  implements View.OnClickListener {
    // 声明控件对象
    private SharedPreferences sp;
    private UserFaceImageView TcImg;
    private String imgUrl;
    private TextView TcName,TcSex,TcXueLi,TcZhiCheng,TcMood,TcExit;
    private LinearLayout llTcMood,llAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_activity_sa_fragment_4, container, false);
        //TODO:123
        //获得实例对象
        sp = getContext().getSharedPreferences("TcInfo",0);//教师登录信息存储器
        TcImg = (UserFaceImageView) view.findViewById(R.id.imv_tc_img);//教师头像
        TcName = (TextView) view.findViewById(R.id.tv_tc_name);//教师姓名
        TcSex = (TextView) view.findViewById(R.id.tv_tc_sex);//教师性别
        TcXueLi = (TextView) view.findViewById(R.id.tv_tc_xueli);//教师学历
        TcZhiCheng = (TextView) view.findViewById(R.id.tv_tc_zhicheng);//教师职称
        llTcMood = (LinearLayout) view.findViewById(R.id.ll_tc_mood);//教师个性签名 行
        llAbout = (LinearLayout) view.findViewById(R.id.ll_tc_about);//关于 行
        TcMood = (TextView) view.findViewById(R.id.tv_tc_mood);//教师个性签名
        TcExit = (TextView) view.findViewById(R.id.tv_app_exit);//退出

        //TcImg;
        //imageLoader = new ImageLoader(,new LruImageCache());
        imgUrl = "http://lzedu-sa.stor.sinaapp.com/Images/Teachers_Face/" + sp.getString("TcID","noHead") + ".jpg";
        GetImg(imgUrl);//获取并显示 头像
        TcName.setText(sp.getString("TcName","姓名"));
        TcSex.setText(sp.getString("TcSex","未知"));
        TcXueLi.setText(sp.getString("TcXueLi","未知"));
        TcZhiCheng.setText(sp.getString("TcZhiCheng","未知"));
        TcMood.setText(sp.getString("TcMood","嗨，今天累坏了吧？"));

        //设置监听器
        TcImg.setOnClickListener(this);
        TcName.setOnClickListener(this);
        llTcMood.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        TcExit.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imv_tc_img:
                //showMessage("姓名："+TcImg.getId());
                //System.out.println("姓名："+TcName.getText().toString());
                startActivity(new Intent(getActivity(), UploadTeacherFacePic.class));
                break;
            case R.id.tv_tc_name:
                //System.out.println("实现教师信息修改和保存即可！！姓名："+TcName.getText().toString());
                break;
            case R.id.ll_tc_mood:
                //System.out.println("实现教师个性签名修改编辑上传！！");
                ToastUtil.showToast(getContext(),"实现教师个性签名修改编辑上传！！");
                break;
            case R.id.ll_tc_about:
                //System.out.println("实现APP检查更新功能！！");
                //ToastUtil.showToast(getContext(),"实现APP检查更新功能！！");
                startActivity(new Intent(getActivity(), AppAboutActivity.class));
                break;
            case R.id.tv_app_exit:
                //getActivity().finish();//结束程序
                exitTips();
                break;
            default:
        }
    }
    /**
     * 获取并显示 头像
     */
    private void GetImg(String imgUrl) {
        Request<Bitmap> request = NoHttp.createImageRequest(imgUrl);
        CallServer.getRequestInstance().add(getContext(), 0, request, new HttpListener<Bitmap>() {
            @Override
            public void onSucceed(int what, Response<Bitmap> response) {
                TcImg.setImageBitmap((Bitmap)response.get());
            }

            @Override
            public void onFailed(int what, Response<Bitmap> response) {
                TcImg.setImageResource(R.drawable.default_userhead);
            }
        }, true, false);
    }
    //退出提示
    private void exitTips(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("退出程序")
                .setMessage("是否退出程序")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();// 关闭对话框
                        getActivity().finish();// 结束程序
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();// 关闭对话框
                            }
                        })
                .create(); //创建对话框
        alertDialog.show(); // 显示对话框
    }
}
