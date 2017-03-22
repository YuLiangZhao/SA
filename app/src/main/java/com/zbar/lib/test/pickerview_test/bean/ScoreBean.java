package com.zbar.lib.test.pickerview_test.bean;


import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by KyuYi on 2017/3/2.
 * E-Mail:kyu_yi@sina.com
 * 功能：
 */

public class ScoreBean implements IPickerViewData {
    int id;
    String Num;

    public ScoreBean(int id, String Num) {
        this.id = id;
        this.Num = Num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNo() {
        return Num;
    }

    public void setCardNo(String Num) {
        this.Num = Num;
    }

    @Override
    public String getPickerViewText() {
        return Num;
    }
}
