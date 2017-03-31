package com.zbar.lib.custom_views.gridview.grid_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbar.lib.R;
import com.zbar.lib.custom_views.gridview.BaseViewHolder;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class Menu1_GridAdapter extends BaseAdapter {
	private Context mContext;

	public String[] Btn_TextArr = {
			"扫二维码",
			"课堂拍照",
			"摇 一 摇",

			"学生管理",
			"评语管理",
			"批量评价",

			"奖品管理",
            "积分兑换",
            "更多..."
	};
	public int[] Btn_ImageArr = {
			R.drawable.btns_qr_scan,
            R.drawable.btns_camera,
			R.drawable.btns_shake,

			R.drawable.btns_st_list,
			R.drawable.btns_gold,
			R.drawable.btns_score_list_add,

			R.drawable.btns_score_shop,
			R.drawable.btns_cash,
            R.drawable.btns_add_more
	};

	public Menu1_GridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Btn_TextArr.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_activity_gridview_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(Btn_ImageArr[position]);

		tv.setText(Btn_TextArr[position]);
		return convertView;
	}

}
