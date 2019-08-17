package com.zbar.lib.app_main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zbar.lib.R;
import com.zbar.lib.app_web.CheckHomeWorkActivity;
import com.zbar.lib.app_web.WebViewActivity;
import com.zbar.lib.camera_scan.QRScanActivity;
import com.zbar.lib.custom_views.gridview.AliPayGridView;
import com.zbar.lib.custom_views.gridview.grid_adapter.Menu2_GridAdapter;
import com.zbar.lib.test.swipemenu_test.SwipeMenuTestMainActivity;
import com.zbar.lib.util.ToastUtil;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ContactsFragment extends Fragment {
    private AliPayGridView Menu_Grid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_activity_sa_fragment_2, container, false);
        Menu_Grid = view.findViewById(R.id.gv_btn2_menu_grid);//教师菜单九宫格
        Menu_Grid.setAdapter(new Menu2_GridAdapter(this.getContext()));
        Menu_Grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                System.out.println("Fragment==2==>位置："+ position + "序号："+ id);
                GridItemClick(position);
            }
        });
        return view;
    }
    //响应：TcMenu_Grid中各种功能图标的单击事件
    public void GridItemClick(int position) {
        switch (position) {
            case 0:
                //ToastUtil.showToast(getContext(),扫二维码："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(),QRScanActivity.class));
                break;
            case 1:
                //ToastUtil.showToast(getContext(),作业查漏："+ position);【作业本和配套练等作业类型在网页内分类查询即可，不在APP中单独列开】
                //跳转界面
                //IE("http://lzedu.sinaapp.com/SA/Student_HomeWork_Checker.php");
                startActivity(new Intent(getActivity(),CheckHomeWorkActivity.class));
                break;
            case 2:
                //ToastUtil.showToast(getContext(),布置作业："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/Teacher_Add_HomeWork.php");
                break;
            case 3:
                //ToastUtil.showToast(getContext(),发布作业："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(),SwipeMenuTestMainActivity.class));
                //IE("http://lzedu.sinaapp.com/SA/Student_List_AddScore.php");
                break;
            case 4:
                //ToastUtil.showToast(getContext(),添加奖品："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Add.php");
                break;
            case 5:
                //ToastUtil.showToast(getContext(),积分兑换："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Logs.php");
                break;
            case 6:
                //ToastUtil.showToast(getContext(),批量加分："+ position);
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
