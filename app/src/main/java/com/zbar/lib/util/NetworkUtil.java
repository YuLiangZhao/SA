package com.zbar.lib.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by ken on 16-7-15.
 *
 * 这个类主要是用来判断网络链接的，是否链接，是否是wi-fi链接，是否是移动网络链接
 */
public class NetworkUtil {
    /*
    Android检查是否已经连接到网络:另外需要权限
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    */
    //检测网络连接状态
    public static boolean isOnLine(Context context){
        ConnectivityManager NetConnManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean isWIFI = NetConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean isGPRS = NetConnManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return (isWIFI | isGPRS);
    }
    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) return false;
        return connectivity.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openNetworkSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName componentName = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 使用SSL不信任的证书
     */
    public static  void useUntrustedCertificate() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 判断是否链接网络
     */
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            //返回链接状态
            return activeInfo != null && activeInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }
    //判断是否是wifi链接
    public static boolean isWifiConnected(Context context) {
        try {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            //返回链接状态
            return activeInfo != null && activeInfo.isConnected()
                    && activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
        } catch (Exception e) {
            return false;
        }
    }
}
