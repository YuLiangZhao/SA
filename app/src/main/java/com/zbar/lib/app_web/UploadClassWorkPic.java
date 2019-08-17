package com.zbar.lib.app_web;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.nohttp.BitmapBinary;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;
import com.zbar.lib.R;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.camera_upload_pic.EditStudentClassWorkPicActivity;
import com.zbar.lib.constant.SinaUrlConstants;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.util.NetworkUtil;

import java.io.File;
import java.io.IOException;


/**
 * Created by Administrator on 2017/1/24.
 */

public class UploadClassWorkPic extends BaseActivity{
    // 声明控件对象
    private SharedPreferences sp;
    private ImageView iv_StClassWorkImg;
    private TextView tv_StClassWorkTips;

    /*图片名称 */
    private static final String PHOTO_FILE_NAME = "StudentClassWorkPic.jpg";
    private static final int CAMERA_REQUEST = 1;
    private static final int PHOTO_EDIT = 2;
    private Uri imageUri;
    private File imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "sa"  + File.separator + PHOTO_FILE_NAME);

    @Override
    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_show_student_class_work_pic);
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        iv_StClassWorkImg = findViewById(R.id.imv_show_student_class_work);//学生课堂学习完成情况图片，可以编辑批注上传至大屏幕
        tv_StClassWorkTips = findViewById(R.id.tv_new_student_class_work_pic_tips);
        //绑定监听
        iv_StClassWorkImg.setOnClickListener(this);

        //
        getPicFromCamera();
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.imv_show_student_class_work:
                getPicFromCamera();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }
    // 检查并获取相机权限、存取权限 后拍照 2019年8月17日18:03:41
    private void getPicFromCamera() {
        String[] CAMERA_PERMISSIONS = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CALENDAR
        };
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //先判断有没有权限 ，没有就在这里进行权限的申请
            ActivityCompat.requestPermissions(this,
                    CAMERA_PERMISSIONS, 1);
        }else{
            TakePhoto();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TakePhoto();
    }
    //从摄像头拍照
    private void TakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //如果是7.0及以上的系统使用FileProvider的方式创建一个Uri
                imageUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", imageFile);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME));
            }
            //Log.e("imageUri:", imageUri.toString());
            //将Uri传递给系统相机
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //showMessage("请求值：" + requestCode);
        switch (requestCode) {
            case CAMERA_REQUEST://1
                //showMessage("resultCode:" + resultCode);
                switch (resultCode) {
                    case -1:// -1表示拍照成功
                        if (imageFile.exists()) {
                            iv_StClassWorkImg.setImageURI(imageUri);
                            photoClip(imageUri);
                        }
                        break;
                    case 1:
                        showMessage("1");
                    default:
                        break;
                }
                break;
            case PHOTO_EDIT://2
                if (data != null) {
                    Uri uri = data.getData();
                    iv_StClassWorkImg.setImageURI(uri);
                    Bitmap photo = null;
                    try {
                        //photo = ImageUtil.getBitmapFormUri(this,uri);//会进一步压缩
                        photo =MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);//保持原图上传
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //异步上传文件
                    //检查网络环境，联网则打开浏览器
                    if(NetworkUtil.isOnLine(getApplicationContext())) {
                        uploadFile(photo);
                    }else{
                        showMessage("手机没网络，请检查WIFI或数据网络环境！");
                        finish();//返回上一层界面
                    }

                }
                break;
            default:
                break;
        }

    }
    //图片涂鸦 标注
    private void photoClip(Uri uri) {
        // 调用自定义的涂鸦编辑器
        Intent intent = new Intent(UploadClassWorkPic.this, EditStudentClassWorkPicActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, PHOTO_EDIT);
    }
    //上传头像
    private void uploadFile(Bitmap photo) {
        //图片上传网址
        String UpUrl = SinaUrlConstants.SAE_UPLOAD_StudentClassWork_URL;
        Request<String> request = new StringRequest(UpUrl, RequestMethod.POST);
        request.add("file",new BitmapBinary(photo,"temp.jpg"));
        CallServer.getRequestInstance().add(this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response response) {
                String url = response.get().toString();
                showMessage(url);
                //添加相关图片操作
                //或者 转向某个专用 图片控制 Activity
                if(!url.equals("")){
                    IE(SinaUrlConstants.SAE_ShowClassPic_URL + "?pic=" + url);
                }
                finish();
            }

            @Override
            public void onFailed(int what, Response response) {
                //
                showMessage("图片上传失败！！！");
            }
        }, true, true);
    }
    //APP内 打开指定网址
    private void IE(String url){
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}