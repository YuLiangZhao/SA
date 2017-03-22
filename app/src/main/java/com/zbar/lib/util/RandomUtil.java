package com.zbar.lib.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/2/22.
 */

public class RandomUtil {
    //获取一个用list展现的随机数的方法
    /**
     *
     * @param datas
     * 目标数列
     * @param num
     * 所需要的数列大小
     * @return
     *用法：
     * ArrayList<Integer> arrayList = RandomUtil.getRandomList(10,8);
     * Log.i("msg" , arrayList.toString());
     * I/msg﹕ [5, 7, 4, 9, 2, 1, 0, 3]
     **/
    public static ArrayList<Integer> getRandomList(ArrayList<Integer> datas , int num)
    {
        Random random = new Random();
        ArrayList<Integer> resultDatas = new ArrayList<Integer>();
        for(int i = 0 ; i < num ; i++){
            int data = Math.abs(random.nextInt() % datas.size());
            resultDatas.add(datas.get(data));
            datas.remove(datas.get(data));
        }
        return resultDatas;
    }
}
