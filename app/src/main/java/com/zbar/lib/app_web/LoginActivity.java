package com.zbar.lib.app_web;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;
import com.zbar.lib.app_main.SA_MainActivity;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.util.NetworkUtil;
import com.zbar.lib.util.StringUtil;

import org.json.JSONObject;

/*
教师登录界面处理
*/
public class LoginActivity extends BaseActivity {
    // 声明控件对象
    private EditText et_name, et_pass;
    private Button mLoginButton,mLoginError,mRegister,ONLYTEST;

    boolean isReLogin=false;
    private int SERVER_FLAG=0;

    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;

    private CheckBox rem_pw;
    private CheckBox auto_login;
    private SharedPreferences sp;
    //
    private String tel ,en_tel;
    private String pwd ,en_pwd;
    private String TcID,TcName,TcSex,TcXueLi,TcZhiCheng,TcMood;

    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_login);
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器

        et_name = (EditText) findViewById(R.id.username);//用户名
        et_pass = (EditText) findViewById(R.id.password);//密码
        bt_username_clear = (Button)findViewById(R.id.bt_username_clear);//清除用户名
        bt_pwd_clear = (Button)findViewById(R.id.bt_pwd_clear);//清除密码
        bt_pwd_eye = (Button)findViewById(R.id.bt_pwd_eye);//密码可见性开关
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);//记住密码
        auto_login = (CheckBox) findViewById(R.id.cb_auto);//自动登录



        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            rem_pw.setChecked(true);
            et_name.setText(sp.getString("USER_NAME", ""));
            et_pass.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                //跳转界面
                //System.out.println("自动登录中...");
                login();
            }
        }
        //元件 单击事件 监听
        rem_pw.setOnClickListener(this);
        auto_login.setOnClickListener(this);
        //
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);

        initWatcher();

        et_name.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);

        mLoginButton = (Button) findViewById(R.id.login);
        mLoginError  = (Button) findViewById(R.id.login_error);
        mRegister    = (Button) findViewById(R.id.register);
        ONLYTEST     = (Button) findViewById(R.id.registfer);

        ONLYTEST.setOnClickListener(this);
        ONLYTEST.setOnLongClickListener(this);
        mLoginButton.setOnClickListener(this);
        mLoginError.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }
    /**
     * 手机号，密码输入控件公用这一个watcher
     */
    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                et_pass.setText("");
                if(s.toString().length()>0){
                    bt_username_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    public void widgetClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:  //正常 登陆 验证
                //检查网络环境，联网则登录
                if(NetworkUtil.isOnLine(getApplicationContext())) {
                    login();
                }else{
                    showMessage("手机没网络，请检查WIFI或数据网络环境！");
                }
                break;
            case R.id.cb_mima:
                if (rem_pw.isChecked()) {
                    //System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }else {
                    //System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
                break;
            case R.id.cb_auto:
                if (auto_login.isChecked()) {
                    //System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
                } else {
                    //System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
                break;
            case R.id.login_error: //无法登陆(忘记密码了吧)
                this.showMessage("忘记密码了吧...");
                break;
            case R.id.register:    //注册新的用户
                this.showMessage("注册新的用户...");
                break;

            case R.id.registfer:
                if(SERVER_FLAG>10){
                    this.showMessage("内部测试--谨慎操作...");
                }
                SERVER_FLAG++;
                break;
            case R.id.bt_username_clear:
                et_name.setText("");
                et_pass.setText("");
                break;
            case R.id.bt_pwd_clear:
                et_pass.setText("");
                break;
            case R.id.bt_pwd_eye:
                if(et_pass.getInputType() == (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                    bt_pwd_eye.setBackgroundResource(R.drawable.button_eye_s);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
                }else{
                    bt_pwd_eye.setBackgroundResource(R.drawable.button_eye_n);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_pass.setSelection(et_pass.getText().toString().length());
                break;
        }
    }
    /**
     * 登陆
     */
    private void login() {
        tel = et_name.getText().toString();
        pwd = et_pass.getText().toString();
        String LoginUrl = "http://lzedu.sinaapp.com/SA/Teacher_Login.php";
        if (StringUtil.isEmpty(tel) ) {
            this.showMessage("请输入手机号码！");
            return;
        }
        if (StringUtil.isEmpty(pwd) ) {
            this.showMessage("请输入密码！");
            return;
        }

        en_tel = StringUtil.Base64Encode(tel);
        en_pwd = StringUtil.Base64Encode(pwd);
        //this.showMessage(tel + "|" + pwd + en_tel + "|" + en_pwd);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(LoginUrl, RequestMethod.POST);
        request.add("tel", en_tel);
        request.add("pwd", en_pwd);
        CallServer.getRequestInstance().add(this, 0, request, new HttpListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response response) {
                //showMessage(response.get().toString());
                JSONObject jsonObject = (JSONObject)response.get();
                try {
                    if (jsonObject.has("TcData")) {
                        JSONObject TcData = jsonObject.getJSONObject("TcData");
                        TcID = TcData.getString("ID");
                        TcName = TcData.getString("Name");
                        TcSex = TcData.getString("Sex");
                        TcXueLi = TcData.getString("XueLi");
                        TcZhiCheng = TcData.getString("ZhiCheng");
                        TcMood = TcData.getString("Mood");

                        //showMessage("登录成功：" + TcID + "==" + TcName);
                        //登录成功和记住密码框为选中状态才保存用户信息
                        if(rem_pw.isChecked()){
                            //记住用户名、密码、
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", tel);
                            editor.putString("PASSWORD",pwd);
                            editor.putString("TcID", TcID);
                            editor.putString("TcName",TcName);
                            editor.putString("TcSex", TcSex);
                            editor.putString("TcXueLi",TcXueLi);
                            editor.putString("TcZhiCheng", TcZhiCheng);
                            editor.putString("TcMood",TcMood);
                            editor.apply();
                        }
                        //跳转界面
                        startActivity(new Intent(LoginActivity.this,SA_MainActivity.class));
                        finish();
                    }else{
                        showMessage("登录失败：用户名或密码错误！");
                    }
                } catch (Exception e) {
                    showMessage("服务器返回值异常："+e.toString());
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                showMessage("程序内部错误："+response.get().toString());
            }
        }, true, true);
    }

    //=================================================================
    @Override
    public boolean widgetLongClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.registfer:
                if(SERVER_FLAG>9){
                    this.showMessage("widgetLongClick...");
                }
                //SERVER_FLAG++;
                break;
        }
        return true;
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(isReLogin){
                Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                LoginActivity.this.startActivity(mHomeIntent);
            }else{
                LoginActivity.this.finish();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
