package com.zbar.lib.app_main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zbar.lib.R;
import com.zbar.lib.app_web.WebViewActivity;
import com.zbar.lib.custom_views.gridview.AliPayGridView;
import com.zbar.lib.custom_views.gridview.grid_adapter.Menu3_GridAdapter;
import com.zbar.lib.util.ToastUtil;

/**
 * Created by Administrator on 2017/2/15.
 */

public class DiscoveryFragment extends Fragment {
    private AliPayGridView Menu_Grid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_activity_sa_fragment_3, container, false);
        Menu_Grid =(AliPayGridView) view.findViewById(R.id.gv_btn3_menu_grid);//教师菜单九宫格
        Menu_Grid.setAdapter(new Menu3_GridAdapter(this.getContext()));
        Menu_Grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                System.out.println("Fragment==3==>位置："+ position + "序号："+ id);
                GridItemClick(position);
            }
        });
        return view;
    }
    //响应：TcMenu_Grid中各种功能图标的单击事件
    public void GridItemClick(int position) {
        switch (position) {
            case 0:
                //ToastUtil.showToast(getContext(),"学生信息："+ position);
                //跳转界面
                ToastUtil.showToast(getContext(),"学生信息..."+ position);//基础列表，在之上做相关操作
                break;
            case 1:
                ToastUtil.showToast(getContext(),"综合查询："+ position);//综合查询相关信息，检索功能
                //跳转界面
                //IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Add.php");
                break;
            case 2:
                ToastUtil.showToast(getContext(),"统计分析："+ position);//综合统计分析报表
                //跳转界面
                //IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Logs.php");
                break;
            case 3:
                ToastUtil.showToast(getContext(),"数据同步："+ position);//主要是学生得分信息同步，照片同步等
                //跳转界面
                //IE("http://lzedu.sinaapp.com/SA/Student_List_AddScore.php");
                break;
            case 4:
                ToastUtil.showToast(getContext(),"数据备份："+ position);
                //跳转界面
                //IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Add.php");
                break;
            case 5:
                ToastUtil.showToast(getContext(),"数据恢复："+ position);
                //跳转界面
                //IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Logs.php");
                break;
            case 60:
                //ToastUtil.showToast(getContext(),"批量加分："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/Student_List_AddScore.php");
                break;
            default:
                ToastUtil.showToast(getContext(),"该功能尚未完成开发..."+ position);
        }
    }
    private void IE(String url){
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
