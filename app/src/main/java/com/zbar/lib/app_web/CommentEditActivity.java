package com.zbar.lib.app_web;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.WheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;
import com.zbar.lib.SerializableMap;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.constant.SinaUrlConstants;
import com.zbar.lib.dialog.TipsDialog;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class CommentEditActivity extends BaseActivity {
    private String TID,RID,sMsg,sNum;
    private SerializableMap map;
    //
    private ImageButton ibTopBack;
    private TextView tvTopTitle;
    private TextView btnSave;
    //
    private final int Max_Input_Char_Len = 15;//评语最大长度是15个字符
    private SharedPreferences sp;
    private TipsDialog tipsDialog;
    private EditText etMsg;
    private TextWatcher etMsg_Watcher;
    private TextView tvMsgTips;
    private String iScore = "0";


    private List<String> scoreAOP,scoreData,scoreMark;
    private WheelView wvAOP,wvScore,wvMark;

    @Override
    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_comments_list_edit);
        //
        //scoreAOP = initAOP();
        scoreData = initData();
        //scoreMark = initMark();
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        TID = sp.getString("TcID","");
        tvTopTitle = findViewById(R.id.tv_top_title);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            map = (SerializableMap) bundle.get("map");
            sMsg = map.getMap().get("Reason").toString();
            RID = map.getMap().get("RID").toString();
            sNum = map.getMap().get("Num").toString();
            tvTopTitle.setText("修改评语");
        }else{
            sMsg = "";
            RID = "";
            sNum = "0";
            tvTopTitle.setText("添加评语");
        }

        ibTopBack = findViewById(R.id.ib_top_back);
        ibTopBack.setOnClickListener(this);

        TipsDialog tipsDialog = new TipsDialog(getActivity());
        etMsg = findViewById(R.id.et_com_msg);
        etMsg.setText(sMsg);
        tvMsgTips = findViewById(R.id.tv_com_msg_tips);
        btnSave = findViewById(R.id.tv_top_right_btn);
        btnSave.setText(R.string.com_btn_submit_text);
        btnSave.setOnClickListener(this);
        btnSave.setEnabled(false);//初始化保存按钮失效

        initWatcher();
        etMsg.addTextChangedListener(etMsg_Watcher);
        etMsg.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Max_Input_Char_Len)});//即限定最大输入字符数为15
        //
        wvScore = findViewById(R.id.wv_score);
        wvScore.setAdapter(new WheelAdapter() {
            @Override
            public int getItemsCount() {
                return scoreData.size();
            }

            @Override
            public Object getItem(int index) {
                return scoreData.get(index);
            }

            @Override
            public int indexOf(Object o) {
                return scoreData.indexOf(o);
                //return  0;
            }
        });
        wvScore.setTextSize(30);
        wvScore.setCurrentItem(scoreData.size()/2);//设置默认选中项
        wvScore.setTextColorCenter(Color.RED);//设置选中项的颜色
        wvScore.setTextColorOut(Color.DKGRAY);//设置未选中项的颜色
        wvScore.setDividerColor(Color.RED);//设置分割线的颜色
        wvScore.setLineSpacingMultiplier(0.5f);//设置两横线之间的间隔倍数
        //wvScore.setDividerType(WheelView.DividerType.FILL);
        wvScore.setDividerType(WheelView.DividerType.WRAP);
        //wvScore.setLabel("分");
        //wvScore.setBackgroundColor(Color.BLACK);//滚轮背景颜色 Night mode
        //wvScore.setMinimumHeight(100);
        wvScore.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                //tvTopTitle.setText(scoreData.get(index));
                iScore = scoreData.get(index);
                String msg = etMsg.getText().toString();
                if(!StringUtil.isEmpty(msg) && !msg.equals("请输入评语")) {
                    //保存按钮可用 绿色
                    btnSave.setEnabled(true);
                    btnSave.setBackgroundResource(R.drawable.corners_green_bg);
                }
            }
        });
    }
    //评分滚轮数据生成器
    public  List<String>  initData() {
        List<String> list = new ArrayList<String>();
        for (int i = -100; i <= 100; i++) {
            if(i<=0) {
                list.add(""+i);
            }else{
                list.add("+"+i);
            }
        }
        return list;
    }
    /**
     * 手机号，密码输入控件公用这一个watcher
     */
    private void initWatcher() {
        etMsg_Watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                int len = s.toString().length();//已输入字符个数
                int left = Max_Input_Char_Len - len;//还能输入几个字符
                Log.i("Left:",s.toString() + " == " + left);
                if(len == 0){
                    //保存按钮不可用 红色
                    btnSave.setEnabled(false);
                    btnSave.setBackgroundResource(R.drawable.corners_red_bg);
                }else {
                    if (left >= 0) {
                        tvMsgTips.setText("你已经输入了" + len + "个字符，还能输入" + left + "个字符。");
                        //保存按钮可用 绿色
                        btnSave.setEnabled(true);
                        btnSave.setBackgroundResource(R.drawable.corners_green_bg);
                    } else {
                        int over = len - Max_Input_Char_Len;
                        tvMsgTips.setText("你已经输入了" + len + "个字符，多输入" + over + "个字符。");
                        btnSave.setText("太多");
                        //保存按钮不可用 红色
                        btnSave.setEnabled(false);
                        btnSave.setBackgroundResource(R.drawable.corners_red_bg);
                    }
                }
            }
        };
    }
    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_back:
                finish();
                break;
            case R.id.tv_top_right_btn:
                //Save Com
                SaveCom();
                break;
            default:
                showMessage(v.toString());
        }
    }
    //Save Com
    public void SaveCom(){
        String msg = etMsg.getText().toString();
        String num = scoreData.get(wvScore.getCurrentItem());
        //showMessage(msg + "【"+num+"】");
        if (StringUtil.isEmpty(msg) ) {
            this.showMessage("请输入评语！");
            return;
        }
        if (StringUtil.isEmpty(num) ) {
            this.showMessage("请选择评分！");
            return;
        }

        String en_msg = StringUtil.Base64Encode(msg);
        String en_num = StringUtil.Base64Encode(num);
        String en_tid = StringUtil.Base64Encode(TID);
        String en_rid = StringUtil.Base64Encode(RID);
        //this.showMessage(RID + "||" + en_rid);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SinaUrlConstants.SAE_SaveCommentUrl, RequestMethod.POST);
        request.add("Reason", en_msg);
        request.add("Num", en_num);
        request.add("TID", en_tid);
        request.add("RID", en_rid);

        CallServer.getRequestInstance().add(this, 0, request, new HttpListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                System.out.println("请求成功:" + response.toString());
                // showMessage(response.toString());
                // 开始解析JSON字符串
                JSONObject jsonObject = response.get();
                try {
                    if (jsonObject.has("Data")) {
                        JSONObject ComData = jsonObject.getJSONObject("Data");
                        int RID = ComData.getInt("RID");
                        if(RID > 0){
                            showMessage("保存评语成功！");
                        }else{
                            showMessage("保存评语失败！");
                        }
                        finish();
                    }else{
                        showMessage("保存评语失败！");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showMessage("保存评语失败！");
                finish();
            }
        }, true, true);
    }
    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            View v=getCurrentFocus();
            boolean  hideInputResult =isShouldHideInput(v,ev);
            //Log.v("hideInputResult","zzz-->>"+hideInputResult);
            if(hideInputResult){
                if (v != null) {
                    v.clearFocus();
                }
                InputMethodManager imm = (InputMethodManager) CommentEditActivity.this
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(imm.isActive()){
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            //之前一直不成功的原因是,getX获取的是相对父视图的坐标,getRawX获取的才是相对屏幕原点的坐标！！！
            //Log.v("leftTop[]","zz--left:"+left+"--top:"+top+"--bottom:"+bottom+"--right:"+right);
            //Log.v("event","zz--getX():"+event.getRawX()+"--getY():"+event.getRawY());
            return !(event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom);
            // 点击的是输入框区域，保留点击etMsg的事件
        }
        return false;
    }
}
