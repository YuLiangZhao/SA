package com.zbar.lib.app_web;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.zbar.lib.R;

/**
 * Created by Administrator on 2017/3/8.
 */

public class WeatherActivity extends Activity {

    private TextView cityname;
    private TextView cityid;
    private TextView temp;
    private TextView weather;
    private TextView ptime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_json_test_weather);

        cityname = (TextView) findViewById(R.id.cityname);
        cityid = (TextView) findViewById(R.id.cityid);
        temp = (TextView) findViewById(R.id.temp);
        weather = (TextView) findViewById(R.id.weather);
        ptime = (TextView) findViewById(R.id.ptime);


        WeatherAsyncTask mAsyncTask = new WeatherAsyncTask(this,cityname,cityid,temp,weather,ptime);
        mAsyncTask.execute();
    }
}
