package com.zbar.lib.camera_upload_pic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zbar.lib.R;
import com.zbar.lib.custom_views.imgview.TYImageView;
import com.zbar.lib.util.ImageUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/2/4.
 */

public class EditStudentClassWorkPicActivity extends Activity implements View.OnClickListener {
    /** Called when the activity is first created. */
    private TYImageView iv_photo;
    private Uri imageUri;
    private Bitmap bgBitmap,upBitmap;
    private TextView btn_finish;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_edit_student_class_work_pic);

        iv_photo = (TYImageView) findViewById(R.id.iv_photo);
        iv_photo.setMode(TYImageView.ModeEnum.TY);
        iv_photo.setTyStrokeWidth(10);//画笔 粗细1-100

        imageUri = getIntent().getData();

        try {
            bgBitmap = ImageUtil.getBitmapFormUri(EditStudentClassWorkPicActivity.this,imageUri);
            iv_photo.setImageBitmap(bgBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn_finish = (TextView) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish: // 完成编辑
                //setResult返回处理后的涂鸦数据
                SetResult();
                break;
            default:
                break;
        }
    }
    //返回值
    public void SetResult(){
        upBitmap = iv_photo.getImageBitmap();
        imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), upBitmap, null,null));
        Log.i("TY：imageUri=",imageUri.toString());
        Intent intent = new Intent();
        intent.setData(imageUri);
        setResult(2, intent);
        finish();
    }
    public Bitmap readBitmapAutoSize(String filePath, int outWidth, int outHeight) {
        // outWidth和outHeight是目标图片的最大宽度和高度，用作限制
        FileInputStream fs = null;
        BufferedInputStream bs = null;
        try {
            fs = new FileInputStream(filePath);
            bs = new BufferedInputStream(fs);
            BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
                    outHeight);
            return BitmapFactory.decodeStream(bs, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bs.close();
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private BitmapFactory.Options setBitmapOption(String file, int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        // 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
        BitmapFactory.decodeFile(file, opt);

        int outWidth = opt.outWidth; // 获得图片的实际高和宽
        int outHeight = opt.outHeight;
        opt.inDither = false;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
        opt.inSampleSize = 1;
        // 设置缩放比,1表示原比例，2表示原来的四分之一....
        // 计算缩放比
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            opt.inSampleSize = sampleSize;
        }
        opt.inJustDecodeBounds = false;// 最后把标志复原
        return opt;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //setResult
        //SetResult();
    }


}