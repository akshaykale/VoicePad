package com.theark.notepadsq;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter_edit extends BaseAdapter {
	Context context;
	String[] drawer_menu_arr;
	int ico[]={R.drawable.ic_action_mic,
				R.drawable.ic_action_cut,
				R.drawable.ic_action_copy,
				R.drawable.ic_action_paste,
				R.drawable.ic_action_select_all,
				R.drawable.ic_action_discard,
				R.drawable.ic_action_search,
				R.drawable.font_96,
				R.drawable.size_96,
				R.drawable.bold_96,
				R.drawable.italic_96,
				R.drawable.underline_96
				};
	public MyAdapter_edit(Context context) {
		this.context=context;
		drawer_menu_arr = context.getResources().getStringArray(R.array.edit_menu);
	}
	@Override
	public int getCount() {
		return drawer_menu_arr.length;
	}

	@Override
	public Object getItem(int pos) {
		return drawer_menu_arr[pos];
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null; //custom view
		if(convertView == null){
			//convert the xml file to java object
			LayoutInflater inflater = (LayoutInflater) context.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.custom_row_edit, parent,false);
		}else{
			row = convertView;
		}
		//create java obj and connect to xml obj
		//TextView tv_title = (TextView) row.findViewById(R.id.row_title);
		ImageView iv_img = (ImageView) row.findViewById(R.id.row_image);
		//set text and image
		//tv_title.setText(drawer_menu_arr[position]);
		iv_img.setImageResource(ico[position]);
		//return the row view
		return row;
	}

}

