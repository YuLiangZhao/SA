package com.zbar.lib.app_web;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/8.
 */

/*===============异步操作类中方法的执行顺序===============*/
//1.生成该类的对象，调条用其execute()方法之后首先执行的是onPreExecute()方法
//2.其次执行 doInBackground(Params...)方法。如果在该方法中每次调用publishProgress(Progress...)方法，都会触发onProgressUpdata(Progress...)方法。
//3.最后执行onPostExecute(Result)方法。

public class WeatherAsyncTask extends
        AsyncTask<String, Integer, List<Map<String, String>>> {
    private TextView cityname = null;
    private TextView cityid = null;
    private TextView temp = null;
    private TextView weather = null;
    private TextView ptime = null;

    public WeatherAsyncTask(Context context, TextView cityname, TextView cityid,
                            TextView temp, TextView weather, TextView ptime) {
        this.cityname = cityname;
        this.cityid = cityid;
        this.temp = temp;
        this.weather = weather;
        this.ptime = ptime;

    }

    // 此函数在另外一个线程中运行，不能操作UI线程中的控件
    protected List<Map<String, String>> doInBackground(String... params) {
        String path = "http://www.weather.com.cn/data/cityinfo/101160101.html";
        //{"weatherinfo":{"city":"兰州","cityid":"101160101","temp1":"1℃","temp2":"18℃","weather":"多云转晴","img1":"n1.gif","img2":"d0.gif","ptime":"18:00"}}
        URL url = null;
        HttpURLConnection httpConnection = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = null;
        System.out.println("异步操作开始");
        try {
            url = new URL(path);
            httpConnection = (HttpURLConnection) url.openConnection();
            if (httpConnection.getResponseCode() == 200) {
                System.out.println("网络已连接");
                // 流转换成为字符串
                InputStream inputStream = httpConnection.getInputStream();
                String strResult = "";
                byte[] b = new byte[1024];
                int i = 0;
                while ((i = inputStream.read(b)) != -1) {
                    strResult += new String(b);
                    b = new byte[1024];
                }

                // 开始解析JSON字符串
                JSONObject jsonObject = new JSONObject(strResult);
                JSONObject jsonObject1 = jsonObject
                        .getJSONObject("weatherinfo");

                map = new HashMap<String, String>();
                String city = jsonObject1.getString("city");
                String cityid = jsonObject1.getString("cityid");
                String temp1 = jsonObject1.getString("temp1");
                String temp2 = jsonObject1.getString("temp2");
                String weather = jsonObject1.getString("weather");
                String img1 = jsonObject1.getString("img1");
                String img2 = jsonObject1.getString("img2");
                String ptime = jsonObject1.getString("ptime");

                map.put("city", city);
                map.put("cityid", cityid);
                map.put("temp1", temp1);
                map.put("temp2", temp2);
                map.put("weather", weather);
                map.put("img1", img1);
                map.put("img2", img2);
                map.put("ptime", ptime);
                list.add(map);

				/*
				// 测试
				System.out.println(city);
				System.out.println(cityid);
				System.out.println(temp1);
				System.out.println(temp2);
				System.out.println(weather);
				System.out.println(img1);
				System.out.println(img2);
				System.out.println(ptime);
				*/

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }

    // 在doInBackground（）方法执行结束之后才开始运行，并且运行在UI线程中，可以对UI线程中的控件进行操作
    protected void onPostExecute(List<Map<String, String>> list) {
        HashMap<String, String> map = new HashMap<String, String>();

        map = (HashMap<String, String>) list.get(0);
        // String city = map.get("city");
        // System.out.println(city);
        cityname.setText(map.get("city"));
        cityid.setText(map.get("cityid"));
        temp.setText(map.get("temp1") + "～" + map.get("temp2"));
        weather.setText(map.get("weather"));
        ptime.setText(map.get("ptime"));

    }

}
