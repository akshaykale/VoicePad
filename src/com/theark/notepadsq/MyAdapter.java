package com.theark.notepadsq;

//#6CC7E5"
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	Context context;
	String[] drawer_menu_arr;
	int ico[]={R.drawable.ic_action_view_as_list,
				R.drawable.ic_draw_open,
				R.drawable.ic_action_save,
				R.drawable.ic_action_settings,
				R.drawable.ic_action_help,
				R.drawable.ic_action_share,
				R.drawable.ic_action_undo_dark};
	public MyAdapter(Context context) {
		this.context=context;
		drawer_menu_arr = context.getResources().getStringArray(R.array.file_menu);
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
			row = inflater.inflate(R.layout.custom_row, parent,false);
		}else{
			row = convertView;
		}
		//create java obj and connect to xml obj
		TextView tv_title = (TextView) row.findViewById(R.id.row_title);
		Typeface custom_font = Typeface.createFromAsset(tv_title.getContext().getAssets(),
				"fonts/whatever_it_takes.ttf");
		tv_title.setTypeface(custom_font);
		ImageView iv_img = (ImageView) row.findViewById(R.id.row_image);
		//set text and image
		tv_title.setText(drawer_menu_arr[position]);
		iv_img.setImageResource(ico[position]);
		//return the row view
		return row;
	}
}


