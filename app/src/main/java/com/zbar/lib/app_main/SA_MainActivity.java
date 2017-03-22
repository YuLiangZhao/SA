package com.zbar.lib.app_main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbar.lib.R;
import com.zbar.lib.adapter.FragmentListAdapter;
import com.zbar.lib.app_web.MeFragment;
import com.zbar.lib.popup_window.TopBarPopWindow;

import java.util.ArrayList;
import java.util.List;


public class SA_MainActivity extends FragmentActivity implements View.OnClickListener {
    //顶端标题栏项目
    private RelativeLayout rlTopBar;
    private TextView tvTitle;
    private ImageButton ibTopAdd,ibTopSearch;
    //
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton rbChat, rbContacts, rbDiscovery, rbMe;

    private List<Fragment> alFragment = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_sa_main);
        rlTopBar = (RelativeLayout) findViewById(R.id.rl_sa_top_bar);
        tvTitle = (TextView) findViewById(R.id.tv_top_title);
        ibTopAdd =  (ImageButton) findViewById(R.id.ib_top_close);
        ibTopSearch =  (ImageButton) findViewById(R.id.ib_top_search);
        ibTopSearch.setOnClickListener(this);
        ibTopAdd.setOnClickListener(this);
        initView();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_top_close:
                TopBarPopWindow popWindow = new TopBarPopWindow(SA_MainActivity.this);
                popWindow.showPopupWindow(findViewById(R.id.ib_top_close));
                break;
            case R.id.ib_top_search:
                tvTitle.setText("搜索");
                break;
            default:
        }
    }
    private void initView() {
        /**
         * RadioGroup部分
         */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbChat = (RadioButton) findViewById(R.id.sa_chat);
        rbContacts = (RadioButton) findViewById(R.id.sa_contacts);
        rbDiscovery = (RadioButton) findViewById(R.id.sa_discovery);
        rbMe = (RadioButton) findViewById(R.id.sa_me);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sa_chat:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        tvTitle.setText(R.string.btn1_text);
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.sa_contacts:
                        tvTitle.setText(R.string.btn2_text);
                        viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.sa_discovery:
                        tvTitle.setText(R.string.btn3_text);
                        viewPager.setCurrentItem(2, true);
                        break;
                    case R.id.sa_me:
                        tvTitle.setText(R.string.btn4_text);
                        viewPager.setCurrentItem(3, true);
                        break;
                }
            }
        });

        /**
         * ViewPager部分
         */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ChatFragment weChatFragment = new ChatFragment();
        ContactsFragment contactsFragment = new ContactsFragment();
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        MeFragment meFragment = new MeFragment();
        alFragment.add(weChatFragment);
        alFragment.add(contactsFragment);
        alFragment.add(discoveryFragment);
        alFragment.add(meFragment);
        //ViewPager设置适配器
        viewPager.setAdapter(new FragmentListAdapter(getSupportFragmentManager(), alFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.sa_chat);
                        break;
                    case 1:
                        radioGroup.check(R.id.sa_contacts);
                        break;
                    case 2:
                        radioGroup.check(R.id.sa_discovery);
                        break;
                    case 3:
                        radioGroup.check(R.id.sa_me);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);//Android按返回键退出程序但不销毁，程序后台运行，同QQ退出处理方式
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

