package com.zbar.lib.app_web;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;

import com.yanzhenjie.nohttp.BitmapBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;
import com.zbar.lib.R;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.constant.SinaUrlConstants;
import com.zbar.lib.custom_views.imgview.UserFaceImageView;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.popup_window.SelectPicPopupWindow;
import com.zbar.lib.util.NetworkUtil;

import java.io.File;


/**
 * Created by Administrator on 2017/1/24.
 */

public class UploadTeacherFacePic extends BaseActivity {
    // 声明控件对象
    private SharedPreferences sp;
    private UserFaceImageView TcImg;
    private String imgUrl;

    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;

    /**
     * 自定义的PopupWindow
     */
    private SelectPicPopupWindow menuWindow;

    /* 头像名称 */
    private String PHOTO_FILE_NAME;
    private Uri imageUri;
    private File imageFile;

    @Override
    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_upload_pic_teacher_face);
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        TcImg = findViewById(R.id.tc_img);//教师头像

        //设置属性
        PHOTO_FILE_NAME = sp.getString("TcID","noHead") + ".jpg";
        imgUrl = "http://lzedu-sa.stor.sinaapp.com/Images/Teachers_Face/" + PHOTO_FILE_NAME;
        imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "sa"  + File.separator + PHOTO_FILE_NAME);

        //绑定监听
        setTcImg();
        TcImg.setOnClickListener(this);

    }
    /**
     * 拍照或从图库选择图片(PopupWindow形式)
     */
    public void showPicturePopupWindow(){
        menuWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 隐藏弹出窗口
                menuWindow.dismiss();
                switch (v.getId()) {
                    case R.id.takePhotoBtn:// 拍照
                        getPicFromCamera();
                        break;
                    case R.id.pickPhotoBtn:// 相册选择图片
                        getPicFromPhoto();
                        break;
                    case R.id.cancelBtn:// 取消
                        break;
                    default:
                        break;
                }
            }
        });
        menuWindow.showAtLocation(findViewById(R.id.layout_student_class_work), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (menuWindow != null) {
            menuWindow.dismiss();
        }
    }
    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.tc_img:
                //showMessage("你点击了：imv_student_class_work");
                showPicturePopupWindow();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }
    //从相册选择
    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }
//    //从摄像头拍照
//    private void getPicFromCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // 下面这句指定调用相机拍照后的照片存储的路径
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
//        startActivityForResult(intent, CAMERA_REQUEST);
//    }
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
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1:// -1表示拍照成功
                        if (imageFile.exists()) {
                            photoClip(imageUri);
                        }

                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        TcImg.setImageBitmap(photo);

                        //异步上传文件
                        //检查网络环境，联网则打开浏览器
                        if(NetworkUtil.isOnLine(getApplicationContext())) {
                            uploadFile(photo);
                        }else{
                            showMessage("手机没网络，请检查WIFI或数据网络环境！");
                            finish();//返回上一层界面
                        }

                    }
                }
                break;
            default:
                break;
        }

    }
    // 调用系统中自带的图片剪裁
    private void photoClip(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }
    //下载头像
    public void setTcImg(){
        Request<Bitmap> request = NoHttp.createImageRequest(imgUrl);
        //获取核心的NoHttp网络工具类对象
        CallServer callSever  = CallServer.getRequestInstance();
        //把队列添加进去
        callSever.add(this, 0, request, new HttpListener<Bitmap>() {
            @Override
            public void onSucceed(int what, Response response) {
                //showMessage(response.get().toString());
                TcImg.setImageBitmap((Bitmap)response.get());
                //finish();
            }

            @Override
            public void onFailed(int what, Response response) {
                TcImg.setImageResource(R.drawable.default_userhead);
            }
        }, true, true);
    }
    //上传头像
    private void uploadFile(Bitmap photo) {
        //图片上传网址
        String UpUrl = SinaUrlConstants.SAE_UPLOAD_UpdateTeacherFace_URL + "?TID=" + sp.getString("TcID","0");
        Request<String> request = new StringRequest(UpUrl, RequestMethod.POST);
        request.add("file",new BitmapBinary(photo,"temp.jpg"));
        CallServer.getRequestInstance().add(this, 1, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response response) {
                showMessage(response.get().toString());
                //finish();
            }

            @Override
            public void onFailed(int what, Response response) {
                //
                showMessage("图片上传失败！");
            }
        }, true, true);
    }


}