package com.zbar.lib.popup_window;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.zbar.lib.R;
import com.zbar.lib.camera_scan.QRScanActivity;
import com.zbar.lib.util.ToastUtil;

/**
 * <p>Title:TopBarPopWindow</p>
 * <p>Description: 自定义PopupWindow</p>
 * =>author syz
 * =>date 2016-3-14
 */
public class TopBarPopWindow extends PopupWindow{
    private View conentView;
    public TopBarPopWindow(final Activity context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_activity_sa_top_bar_pop_window, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置PopupWindow的View
        this.setContentView(conentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 40);
        // 设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow弹出窗体可点击
        this.setFocusable(true);// 设置此参数获得焦点，否则无法点击
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置点击窗口外边窗口消失
        this.setOutsideTouchable(true);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.TopBarPopupAnimation);

        conentView.findViewById(R.id.id_groupchat).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something you need here
                ToastUtil.showToast(context,"联系家长");
                dismiss();
            }
        });
        conentView.findViewById(R.id.id_addfrd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something before signing out
                //context.finish();
                ToastUtil.showToast(context,"添加学生");
                dismiss();
            }
        });
        conentView.findViewById(R.id.id_find).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something you need here
                //ToastUtil.showToast(context,"扫一扫");
                dismiss();
                v.getContext().startActivity(new Intent(v.getContext(),QRScanActivity.class));
            }
        });
        conentView.findViewById(R.id.id_feedback).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something you need here
                ToastUtil.showToast(context,"群发短信");
                dismiss();
            }
        });
        // conentView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        conentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = conentView.findViewById(R.id.id_feedback).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //ToastUtil.showToast(v.getContext(),"y="+y+"height="+height);
                    Log.i("onTouch","y="+y+"height="+height);
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * =>param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 2);
            //this.showAsDropDown(parent, 0, 0);//设置显示PopupWindow的位置位于View的左下方，x,y表示坐标偏移量
            //this.showAtLocation(parent, 0, 0, 0);//设置显示PopupWindow的位置位于View的左下方，x,y表示坐标偏移量
        } else {
            this.dismiss();
        }
    }
}
