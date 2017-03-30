package com.zbar.lib.app_web;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

/**
 * Created by tangbin on 16/5/12.
 */
public class MyWebChromeClient extends WebChromeClient {

    private WebCall webCall;

    public void setWebCall(WebCall webCall) {
        this.webCall = webCall;
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (webCall != null)
            webCall.fileChose(uploadMsg);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    // For Android > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }
    /*
    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        if (webCall != null)
            webCall.fileChose5(filePathCallback);
        return super.onShowFileChooser(webView, filePathCallback,
                fileChooserParams);
    }
    */
    public interface WebCall {
        void fileChose(ValueCallback<Uri> uploadMsg);

        void fileChose5(ValueCallback<Uri[]> uploadMsg);
    }

    //


}
