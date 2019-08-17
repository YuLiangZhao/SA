/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 baoyongzhang <zbar.lib94@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zbar.lib.app_web;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.JsonObjectRequest;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;
import com.zbar.lib.SerializableMap;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.constant.SinaUrlConstants;
import com.zbar.lib.custom_views.swipemenulistview.BaseSwipListAdapter;
import com.zbar.lib.custom_views.swipemenulistview.SwipeMenu;
import com.zbar.lib.custom_views.swipemenulistview.SwipeMenuCreator;
import com.zbar.lib.custom_views.swipemenulistview.SwipeMenuItem;
import com.zbar.lib.custom_views.swipemenulistview.SwipeMenuListView;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.util.ArrayUtil;
import com.zbar.lib.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * SwipeMenuListView
 * Created by zbar.lib on 15/6/29.
 */
public class CommentListActivity extends BaseActivity {
    private String TID;
    private SharedPreferences sp;
    private ImageButton ibTopBack,ibAddBtn;
    private TextView tvTitle;
    private List<Map<String, Object>> mAppList = null;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;

    @Override
    public void initWidget() {
        this.setAllowFullScreen(false);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_comments_list_main);
        //获得实例对象
        sp = this.getSharedPreferences("TcInfo",MODE_PRIVATE);//教师登录信息存储器
        TID = sp.getString("TcID","");
        ibTopBack = findViewById(R.id.ib_top_back);
        tvTitle = findViewById(R.id.tv_top_title);
        ibAddBtn = findViewById(R.id.ib_top_plus);// + 号 按钮
        ibTopBack.setOnClickListener(this);
        ibAddBtn.setOnClickListener(this);
        //SwipeMenuListView
        mListView = findViewById(R.id.slv_comment);
        //
        MyTask mAsyncTask = new MyTask();
        mAsyncTask.execute();
        //


    }
    /*===============异步操作类中方法的执行顺序===============*/
    //1.生成该类的对象，调条用其execute()方法之后首先执行的是onPreExecute()方法
    //2.其次执行 doInBackground(Params...)方法。如果在该方法中每次调用publishProgress(Progress...)方法，都会触发onProgressUpdata(Progress...)方法。
    //3.最后执行onPostExecute(Result)方法。
    private class MyTask extends AsyncTask<String, Void, List<Map<String, Object>>> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //super.onPreExecute();
            //showMessage("请求开始");
            //tipsDialog.setMessage("开始请求");
            //tipsDialog.show();
        }
        // 此函数在另外一个线程中运行，不能操作UI线程中的控件
        @Override
        protected List<Map<String, Object>> doInBackground(String... params) {
            // TODO Auto-generated method stub
            //System.out.println("异步操作开始");
            String TID = sp.getString("TcID","0");
            //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            /*
             * 获取服务器评语数据
             */
            Request<JSONObject> request = new JsonObjectRequest(SinaUrlConstants.SAE_GetCommentJsonUrl, RequestMethod.POST);
            request.add("TID", TID);
            Response<JSONObject> response = NoHttp.startRequestSync(request);
            if (response.isSucceed()) {
                System.out.println("请求成功:" + response.toString());
                // 开始解析JSON字符串
                JSONObject jsonObject = response.get();
                try {
                    if (jsonObject.has("Data")) {
                        JSONArray ComData = jsonObject.getJSONArray("Data");
                        Integer len = ComData.length();
                        if (len > 0) {
                            String jsonString = ComData.toString();
                            mAppList = ArrayUtil.getList(jsonString);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return mAppList;
        }
        // 在doInBackground（）方法执行结束之后才开始运行，并且运行在UI线程中，可以对UI线程中的控件进行操作
        @Override
        protected void onPostExecute(List<Map<String, Object>> result) {
            // TODO Auto-generated method stub
            //super.onPostExecute(result);
            //showMessage("请求结束");
            if(result != null) {
                mAdapter = new AppAdapter();
                mAdapter.setData(result);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                initView();
            }else{
                showMessage("请求失败：服务器返回值异常！");
            }
        }

    }
    //
    private void initView(){
        // step 1. create more_pic_2_gif_loading MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "top" item
                SwipeMenuItem topItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                //topItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                topItem.setBackground(new ColorDrawable(Color.BLUE));
                // set item width
                topItem.setWidth(dp2px(90));
                // set item title
                topItem.setTitle("置顶");
                // set item title fontsize
                topItem.setTitleSize(18);
                // set item title font color
                topItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(topItem);

                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set more_pic_2_gif_loading icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map item = mAppList.get(position);
                //showMessage("position:"+ position + "||item="+item);
                switch (index) {
                    case 0:
                        // top
                        TopCom(item);
                        //showMessage("默认值item="+item);
                        break;
                    case 1:
                        // edit
                        EditCom(item);
                        //showMessage("编辑item="+item);
                        break;
                    case 2:
                        // delete
                        DelCom(item);
                        mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        //showMessage("position:"+ position + "删除item="+item);
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		slv_comment.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                //Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }
    //置顶item
    public void TopCom(Map item){
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SinaUrlConstants.SAE_TopCommentUrl, RequestMethod.POST);
        String RID = StringUtil.Base64Encode(item.get("RID").toString());
        request.add("RID", RID);
        request.add("TID", StringUtil.Base64Encode(TID));
        CallServer.getRequestInstance().add(this, 0, request, new HttpListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                System.out.println("请求成功:" + response.toString());
                // 开始解析JSON字符串
                JSONObject jsonObject = response.get();
                try {
                    if (jsonObject.has("Data")) {
                        JSONArray ComData = jsonObject.getJSONArray("Data");
                        String jsonString = ComData.toString();
                        mAppList = ArrayUtil.getList(jsonString);
                        mAdapter.setData(mAppList);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showMessage("设为默认失败！");
            }
        }, true, true);
    }
    //编辑item
    public void EditCom(Map item){
        //
        Intent intent = new Intent(this,CommentEditActivity.class);
        //传递数据
        final SerializableMap myMap = new SerializableMap();
        myMap.setMap(item);//将map数据添加到封装的myMap中
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", myMap);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void AddCom(){
        Map<String,Object> item = null;
        item.put("RID","");
        item.put("Reason","");
        item.put("Num","");
        EditCom(item);
    }
    //删除item
    public void DelCom(Map item){
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(SinaUrlConstants.SAE_DelCommentUrl, RequestMethod.POST);
        String RID = StringUtil.Base64Encode(item.get("RID").toString());
        request.add("RID", RID);
        request.add("TID", StringUtil.Base64Encode(TID));
        CallServer.getRequestInstance().add(this, 0, request, new HttpListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                System.out.println("请求成功:" + response.toString());
                // 开始解析JSON字符串
                JSONObject jsonObject = response.get();
                try {
                    if (jsonObject.get("err").equals(0)) {
                        showMessage("删除成功！");
                    }else{
                        showMessage("删除失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showMessage("删除失败！");
            }
        }, true, true);
    }
    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_plus:
                //showMessage("+");
                startActivity(new Intent(getActivity(), CommentEditActivity.class));
                //finish();
                break;
            case R.id.ib_top_back:
                finish();
                break;
            default:
        }
    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }

    private class AppAdapter extends BaseSwipListAdapter {

        private List<Map<String, Object>> ListData = null ;

        public void setData(List<Map<String, Object>> data){
            this.ListData = data;
        }

        @Override
        public int getCount() {
            return ListData.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return ListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.layout_activity_comments_list_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Map<String, Object> item = getItem(position);
            int Num = Integer.parseInt(item.get("Num").toString());
            String key = "";
            if(Num > 0){
                key = "+" + Num;
                holder.tv_score.setText(key);
                //改变背景颜色
                holder.tv_score.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_green_bg));
            }else{
                key = "" + Num;
                holder.tv_score.setText(key);
                //改变背景颜色
                holder.tv_score.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_red_bg));
            }
            int now = Integer.parseInt(item.get("Checked").toString());
            if(now == 1){
                holder.tv_score.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_blue_bg));
            }
            String msg = item.get("Reason").toString();
            holder.tv_name.setText(msg);


            holder.tv_score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CommentListActivity.this, "tv_score_click", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CommentListActivity.this,"tv_score_click",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_score;
            TextView tv_name;

            public ViewHolder(View view) {
                tv_score = view.findViewById(R.id.tv_score);
                tv_name = view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

        @Override
        public boolean getSwipEnableByPosition(int position) {
            //if(position % 2 == 0){
                //return false;
            //}
            return true;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
