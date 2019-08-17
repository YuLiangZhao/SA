package com.zbar.lib.guide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zbar.lib.R;
import com.zbar.lib.adapter.FragmentListAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class GuideMainActivity extends FragmentActivity {

    private ViewPager viewPage;
    private GuidePage1Fragment mGuidePage1Fragment;
    private GuidePage2Fragment mGuidePage2Fragment;
    private GuidePage3Fragment mGuidePage3Fragment;
    private GuidePage4Fragment mGuidePage4Fragment;
    private PagerAdapter mPgAdapter;
    private RadioGroup dotLayout;
    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private boolean isFirst= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.layout_activity_guide_group);
        SharedPreferences sp= getSharedPreferences("FirstLaunch",MODE_PRIVATE);
        isFirst=sp.getBoolean("FirstValue",true);//第一次获取不到值，取默认值true
        if(isFirst){

        }else{
            Intent intent=new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            this.finish();//不加这个回返回到引导页，切记！切记！！
        }

        initView();
        viewPage.setOnPageChangeListener(new MyPagerChangeListener());

    }

    private void initView() {
        dotLayout = findViewById(R.id.ad_point_group);
        viewPage = findViewById(R.id.viewPager);
        mGuidePage1Fragment = new GuidePage1Fragment();
        mGuidePage2Fragment = new GuidePage2Fragment();
        mGuidePage3Fragment = new GuidePage3Fragment();
        mGuidePage4Fragment = new GuidePage4Fragment();
        mListFragment.add(mGuidePage1Fragment);
        mListFragment.add(mGuidePage2Fragment);
        mListFragment.add(mGuidePage3Fragment);
        mListFragment.add(mGuidePage4Fragment);
        mPgAdapter = new FragmentListAdapter(getSupportFragmentManager(),mListFragment);
        viewPage.setAdapter(mPgAdapter);

    }

    public class MyPagerChangeListener implements OnPageChangeListener {

        public void onPageSelected(int position) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
            ((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
        }

    }

}