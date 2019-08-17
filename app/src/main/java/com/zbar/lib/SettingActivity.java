package com.zbar.lib;

import android.view.View;
import android.widget.SimpleAdapter;

import com.zbar.lib.app_root.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends BaseActivity {

    private CornerListView cornerListView = null;

    private List<Map<String, String>> listData = null;
    private SimpleAdapter adapter = null;

    @Override
    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_app_setting);

        cornerListView = findViewById(R.id.setting_list);
        setListData();
        //为 layout_list_item 绑定设配器
        adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.layout_activity_quick_score_list_item ,
                new String[]{"text"}, new int[]{R.id.tv_student_name});
        cornerListView.setAdapter(adapter);
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }
    /**
     * 设置列表数据
     */
    private void setListData(){
        listData = new ArrayList<>();

        Map<String,String> map = new HashMap<>();
        map.put("text", "分享");
        listData.add(map);

        map = new HashMap<>();
        map.put("text", "检查新版本");
        listData.add(map);

        map = new HashMap<>();
        map.put("text", "反馈意见");
        listData.add(map);

        map = new HashMap<>();
        map.put("text", "关于我们");
        listData.add(map);

        map = new HashMap<>();
        map.put("text", "支持我们，请点击这里的广告");
        listData.add(map);
    }

}
