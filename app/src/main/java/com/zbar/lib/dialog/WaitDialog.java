package com.zbar.lib.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * date:2016/12/14
 * author:耿佳伟
 * function:自定义一个进度条对话框
 */
public class WaitDialog extends ProgressDialog {
    public WaitDialog(Context context) {
        super(context);
        //设置当前的Activity无Title,并且全屏(点用这个方法必须在setContentView之前)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置对话框的外面点击,是否让对话框消失,false是可以取消
        setCanceledOnTouchOutside(false);
        //设置对话框的样式
        //STYLE_SPINNER为圆形不确定进度条
        //STYLE_HORIZONTAL为条形进图条，要在show()函数调用之后再操作。
        setProgressStyle(STYLE_SPINNER);
        //设置进度条显示的内容
        setMessage("正在请求网络,请稍候...");
    }
    public WaitDialog(Context context, int theme) {
        super(context, theme);
    }
}
