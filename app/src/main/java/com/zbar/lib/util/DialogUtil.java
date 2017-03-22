package com.zbar.lib.util;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/2/22.
 */

public class DialogUtil {
    //弹出框提醒
    public static Dialog showDialog(Context context, int layViewID, int ThemeType) {
        Dialog res = new Dialog(context, ThemeType);
        res.setContentView(layViewID);
        return res;
    }
}
