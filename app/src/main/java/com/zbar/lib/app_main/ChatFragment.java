package com.zbar.lib.app_main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zbar.lib.R;
import com.zbar.lib.app_web.CommentListActivity;
import com.zbar.lib.app_web.ScoreShopListActivity;
import com.zbar.lib.app_web.ShakeActivity;
import com.zbar.lib.app_web.UploadClassWorkPic;
import com.zbar.lib.app_web.WebViewActivity;
import com.zbar.lib.camera_scan.QRScanActivity;
import com.zbar.lib.custom_views.gridview.AliPayGridView;
import com.zbar.lib.custom_views.gridview.grid_adapter.Menu1_GridAdapter;
import com.zbar.lib.test.pickerview_test.PickerViewTestActivity;
import com.zbar.lib.util.ToastUtil;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ChatFragment extends Fragment {
    private AliPayGridView Menu_Grid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_activity_sa_fragment_1, container, false);
        Menu_Grid = view.findViewById(R.id.gv_btn1_menu_grid);//教师菜单九宫格
        Menu_Grid.setAdapter(new Menu1_GridAdapter(this.getContext()));
        Menu_Grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                System.out.println("Fragment==1==>位置："+ position + "序号："+ id);
                GridItemClick(position);
            }
        });
        return view;
    }
    //响应：TcMenu_Grid中各种功能图标的单击事件
    public void GridItemClick(int position) {
        switch (position) {
            case 0:
                //ToastUtil.showToast(getContext(),"扫二维码："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(),QRScanActivity.class));
                break;
            case 1:
                //ToastUtil.showToast(getContext(),"课堂拍照："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/SA_PPT_Controlor.php");
                break;
            case 2:
                //ToastUtil.showToast(getContext(),"课堂拍照："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(), UploadClassWorkPic.class));
                break;
            case 3:
                //ToastUtil.showToast(getContext(),"随机叫号："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(),ShakeActivity.class));
                break;
            case 4:
                //ToastUtil.showToast(getContext(),"学生管理："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/SA_Student_Quick_Score.php");
                //startActivity(new Intent(getActivity(),CommentListActivity.class));
                break;

            case 5:
                //ToastUtil.showToast(getContext(),"批量加分："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/Student_List_AddScore.php");
                break;
            case 6:
                //ToastUtil.showToast(getContext(),"奖品管理："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(),ScoreShopListActivity.class));
                //IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Add.php");
                break;
            case 7:
                //ToastUtil.showToast(getContext(),"积分兑换："+ position);
                //跳转界面
                IE("http://lzedu.sinaapp.com/SA/Student_ScoreShop_Logs.php");
                break;
            case 8:
                //ToastUtil.showToast(getContext(),"评语管理："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(),CommentListActivity.class));
                break;
            case 9:
                //ToastUtil.showToast(getContext(),"更多："+ position);
                //跳转界面
                startActivity(new Intent(getActivity(), PickerViewTestActivity.class));
                break;

            default:
                ToastUtil.showToast(getContext(),"该功能尚未完成开发..."+ position);
        }
    }
    //APP内 打开指定网址
    private void IE(String url){
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
