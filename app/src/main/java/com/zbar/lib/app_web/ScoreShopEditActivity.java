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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.nohttp.BitmapBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;
import com.zbar.lib.R;
import com.zbar.lib.SerializableMap;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.constant.SinaUrlConstants;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.popup_window.SelectPicPopupWindow;
import com.zbar.lib.util.ImageUtil;
import com.zbar.lib.util.NetworkUtil;
import com.zbar.lib.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/29.
 */

public class ScoreShopEditActivity extends BaseActivity {
    private String TID,ID,pName,pNum,pPrice,pImg;
    private SerializableMap map;
    private SharedPreferences sp;
    private ImageButton ibTopBack;
    private TextView tvRightBtn,tvTitle,tvUrl;
    private EditText etName,etScore,etNum;
    private ImageView imvPic;

    //
    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    /**
     * 自定义的PopupWindow
     */
    private SelectPicPopupWindow menuWindow;

    /* 头像名称 */
    private String PHOTO_FILE_NAME = "tmp.jpg";
    private Uri imageUri;
    private File imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "sa"  + File.separator + PHOTO_FILE_NAME);

    @Override
    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_score_shop_edit);
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        TID = sp.getString("TcID","");
        ibTopBack = findViewById(R.id.ib_top_back);
        tvTitle = findViewById(R.id.tv_top_title);
        tvRightBtn = findViewById(R.id.tv_top_right_btn);// + 号 按钮
        ibTopBack.setOnClickListener(this);
        tvRightBtn.setOnClickListener(this);
        //
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            map = (SerializableMap) bundle.get("map");
            ID = map.getMap().get("ID").toString();//当修改奖品信息时，会有ID传入
            pName = map.getMap().get("pName").toString();
            pPrice = map.getMap().get("pPrice").toString();
            pNum = map.getMap().get("pNum").toString();
            pImg = map.getMap().get("pImg").toString();
            tvTitle.setText("修改奖品信息");
        }else{
            ID = "";
            pName = "";
            pPrice = "";
            pNum = "";
            tvTitle.setText("添加奖品");
        }

        etName = findViewById(R.id.et_shop_name);
        etScore = findViewById(R.id.et_shop_score);
        etNum = findViewById(R.id.et_shop_num);
        etName.setText(pName);
        etScore.setText(pPrice);
        etNum.setText(pNum);

        imvPic = findViewById(R.id.imv_shop_pic);
        imvPic.setOnClickListener(this);
        //
        tvUrl = findViewById(R.id.tv_shop_pic_url);
        tvUrl.setText(pImg);
        setShopPic();//设置编辑时的图片
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_back:
                finish();
                break;
            case R.id.tv_top_right_btn:
                //showMessage("Save");
                SaveScoreShop();
                break;
            case R.id.imv_shop_pic:
                //showMessage("AddPic");
                showPicturePopupWindow();
                break;
            default:
        }
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
                            uploadPic(imageUri);//异步上传文件
                        }

                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
                    Uri uri = data.getData();
                    uploadPic(uri);//异步上传文件
                }
                break;
            default:
                break;
        }

    }
    //
    private void uploadPic(Uri uri){
        //
        Bitmap photo = null;
        try {
            photo = ImageUtil.getBitmapFormUri(ScoreShopEditActivity.this,uri);//保持原图上传
        } catch (IOException e) {
            e.printStackTrace();
        }
        imvPic.setImageBitmap(photo);//设置预览图片
        //检查网络环境，联网则打开浏览器
        if(NetworkUtil.isOnLine(getApplicationContext())) {
            uploadFile(photo);
        }else{
            showMessage("手机没网络，请检查WIFI或数据网络环境！");
            finish();//返回上一层界面
        }
    }

    //上传头像
    private void uploadFile(Bitmap photo) {
        //图片上传网址
        String UpUrl = SinaUrlConstants.SAE_UPLOAD_ScoreShopPic_URL + "?TID=" + sp.getString("TcID","0");
        Request<String> request = new StringRequest(UpUrl, RequestMethod.POST);
        request.add("file",new BitmapBinary(photo,"temp.jpg"));
        CallServer.getRequestInstance().add(this, 1, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response response) {
                String url = response.get().toString();
                //showMessage(url);
                tvUrl.setText(url);
                //finish();
            }

            @Override
            public void onFailed(int what, Response response) {
                String msg = "图片上传失败，请重新上传！";
                //showMessage(msg);
                tvUrl.setText(msg);
            }
        }, true, true);
    }
    //保存奖品信息
    private Boolean SaveScoreShop(){
        Boolean flag = true;
        String sName = etName.getText().toString();
        if(StringUtil.isEmpty(sName)){
            showMessage("奖品名称不能为空！");
            return false;
        }
        String sScore = etScore.getText().toString();
        if(StringUtil.isEmpty(sScore)){
            showMessage("奖品售价不能为空！");
            return false;
        }
        String sNum = etNum.getText().toString();
        if(StringUtil.isEmpty(sNum)){
            showMessage("奖品库存不能为空！");
            return false;
        }
        String sUrl = tvUrl.getText().toString();
        if(StringUtil.isEmpty(sUrl)){
            showMessage("请添加奖品图片！");
            return false;
        }
        showMessage(sName + sScore + sNum + sUrl);
        saveShop(sName,sScore,sNum,sUrl);
        return true;
    }
    //保存奖品信息
    private void saveShop(String sName,String sScore,String sNum,String sUrl){
        ID = StringUtil.Base64Encode(ID);
        TID = StringUtil.Base64Encode(TID);
        sName = StringUtil.Base64Encode(sName);
        sScore = StringUtil.Base64Encode(sScore);
        sNum = StringUtil.Base64Encode(sNum);
        sUrl = StringUtil.Base64Encode(sUrl);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SinaUrlConstants.SAE_SaveScoreShop_URL, RequestMethod.POST);
        request.add("ID",ID);
        request.add("TID",TID);
        request.add("pName",sName);
        request.add("pPrice",sScore);
        request.add("pNum",sNum);
        request.add("pImg",sUrl);
        //获取核心的NoHttp网络工具类对象
        CallServer callSever  = CallServer.getRequestInstance();
        //把队列添加进去
        callSever.add(this, 0, request, new HttpListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                System.out.println("请求成功:" + response.toString());
                // 开始解析JSON字符串
                JSONObject jsonObject = response.get();
                try {
                    if (jsonObject.get("err").equals("0")) {
                        showMessage("保存奖品信息成功！");
                    }else{
                        showMessage("保存奖品信息失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                //showMessage(response.toString());
                System.out.println("请求失败:" + response.toString());
                finish();
            }
        },true,true);
    }
    //下载头像
    public void setShopPic(){
        String imgUrl;//根据添加或修改类型设置图片路径
        if(StringUtil.isEmpty(pImg)){
            imvPic.setImageResource(R.drawable.up_pic_add_new_photo);//新增
        }else {
            imgUrl = "http://lzedu-sa.stor.sinaapp.com/UploadFiles/pImg/" + pImg;//修改
            Request<Bitmap> request = NoHttp.createImageRequest(imgUrl);
            //获取核心的NoHttp网络工具类对象
            CallServer callSever = CallServer.getRequestInstance();
            //把队列添加进去
            callSever.add(this, 0, request, new HttpListener<Bitmap>() {
                @Override
                public void onSucceed(int what, Response response) {
                    //showMessage(response.get().toString());
                    imvPic.setImageBitmap((Bitmap) response.get());
                    //finish();
                }

                @Override
                public void onFailed(int what, Response response) {
                    imvPic.setImageResource(R.drawable.up_pic_add_new_photo);
                }
            }, true, true);
        }
    }
}
