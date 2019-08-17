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
 * =>Description:gridview的Adapter
 * =>author http://blog.csdn.net/finddreams
 */
public class Menu3_GridAdapter extends BaseAdapter {
	private Context mContext;

	public String[] Btn_TextArr = {
			"学生信息",
			"教师信息",
			"班级信息",

			"数据导入",
			"综合查询",
			"统计分析",

            "数据同步",
            "数据备份",
            "数据恢复",

            "更多..."
	};
	public int[] Btn_ImageArr = {
			R.drawable.btns_db_st_info,
			R.drawable.btns_db_tc_info,
			R.drawable.btns_db_class_info,

			R.drawable.btns_db_import,
			R.drawable.btns_db_search,
			R.drawable.btns_db_spass,

			R.drawable.btns_db_sync,
            R.drawable.btns_db_backup,
            R.drawable.btns_db_restore,

            R.drawable.btns_add_more
	};

	public Menu3_GridAdapter(Context mContext) {
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
