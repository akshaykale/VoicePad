package com.theark.notepadsq;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	public static final String SET_THEAM = "set_theam";
	public static final String SET_VKB = "set_vkb";
	public static final String SET_ORI = "set_ori";
	public static final String SET_TEXT_FORMATING = "set_text_formating";
	public static final String SET_FONT = "set_font";
	public static final String SIZE_OF_TEXT = "set the text size of the edit text";
	public static final String SET_FULLSCREEN = "set_the_fullScreen";
	protected static final String SET_DEMO = "show_demo_at_startup";
	protected static final String SET_ALIGNMENT = "set_the_alignment";
	//Textviews
	TextView[] tv_ui_text,tv_ui_line; 
	TextView tv_seekbar;
	//Voice Text Formating
	Switch sw_voice;
	//Orientation
	RadioButton rb_ori_land,rb_ori_port,rb_ori_auto;
	//Theams
	RadioButton rb_th_OW,rb_th_BW,rb_th_G;
	//Fonts
	RadioButton rb_fonts_1,rb_fonts_2,rb_fonts_3,rb_fonts_4,rb_fonts_5;
	//Size
	SeekBar sb_size;
	//FullScreen
	Switch sw_fullScreen;
	//Demo
	Switch sw_demo;
	//Aligh
	RadioButton rb_align_l,rb_align_c,rb_align_r;
	
	
	
	//SharedPref
	SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		
		//connect java obj to the ids
		init();
		//Title bar setting
		init_TB();
		//set Font Menu
		init_Font();
		//set ON/OF and radio bt attribute
		setAttri();//set the UI theam
		//set Theams
		SETTheams();
		//set Orientation
		SETOri();
		//Text Formating ON/OF
		SETTextFormating();
		//set font in Main Activity
		SETFont();
		//Set Size in Main Activity
		SETSize();
		//set FullScreen
		SETFullScreen();
		//set DEMO
		SETDemo();
		//setAlignment
		SETAlignment();
		
	}




	




	@SuppressLint("NewApi")
	private void init_TB() {//title bar setting
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EBEBEB")));
		bar
		.setTitle(Html.fromHtml("<font color=\"#564F47\">" + getString(R.string.app_name) + "</font>"));
	}
    
	private void init() {
		tv_ui_line = new TextView[8];
		tv_ui_text = new TextView[8];
		//TVs text
		tv_ui_text[0] = (TextView) findViewById(R.id.set_TextView01);
		tv_ui_text[1] = (TextView) findViewById(R.id.set_TextView03);
		tv_ui_text[2] = (TextView) findViewById(R.id.set_TextView05);
		tv_ui_text[3] = (TextView) findViewById(R.id.set_TextView07);
		tv_ui_text[4] = (TextView) findViewById(R.id.set_TextView09);
		tv_ui_text[5] = (TextView) findViewById(R.id.set_TextView11);
		tv_ui_text[6] = (TextView) findViewById(R.id.set_TextView13);
		tv_ui_text[7] = (TextView) findViewById(R.id.set_TextView15);
		//TVs text
		tv_ui_line[0] = (TextView) findViewById(R.id.set_TextView02);
		tv_ui_line[1] = (TextView) findViewById(R.id.set_TextView04);
		tv_ui_line[2] = (TextView) findViewById(R.id.set_TextView06);
		tv_ui_line[3] = (TextView) findViewById(R.id.set_TextView08);
		tv_ui_line[4] = (TextView) findViewById(R.id.set_TextView10);
		tv_ui_line[5] = (TextView) findViewById(R.id.set_TextView12);
		tv_ui_line[6] = (TextView) findViewById(R.id.set_TextView14);
		tv_ui_line[7] = (TextView) findViewById(R.id.set_TextView16);
		
		//VKb
		sw_voice = (Switch) findViewById(R.id.set_switch_voice);
		//cb_virtualKB = (CheckBox) findViewById(R.id.CheckBox02);
		//fonts
		rb_fonts_1 = (RadioButton) findViewById(R.id.set_font_1);
		rb_fonts_2 = (RadioButton) findViewById(R.id.set_font_2);
		rb_fonts_3 = (RadioButton) findViewById(R.id.set_font_3);
		rb_fonts_4 = (RadioButton) findViewById(R.id.set_font_4);
		rb_fonts_5 = (RadioButton) findViewById(R.id.set_font_5);
		//orientation
		rb_ori_land = (RadioButton) findViewById(R.id.set_ori_land);
		rb_ori_port = (RadioButton) findViewById(R.id.set_ori_port);
		rb_ori_auto = (RadioButton) findViewById(R.id.set_ori_auto);
		//theam
		rb_th_OW = (RadioButton) findViewById(R.id.set_theam_OW);
		rb_th_BW = (RadioButton) findViewById(R.id.set_theam_BW);
		//size
		sb_size = (SeekBar) findViewById(R.id.seekBar1);
		tv_seekbar = (TextView) findViewById(R.id.tv_seekbar);
		//FullScreen
		sw_fullScreen = (Switch) findViewById(R.id.set_switch_fullscreen);
		//Demo
		sw_demo = (Switch) findViewById(R.id.set_switch_demo);
		//Alignment
		//fonts
		rb_align_l = (RadioButton) findViewById(R.id.set_align_left);
		rb_align_c = (RadioButton) findViewById(R.id.set_align_centre);
		rb_align_r = (RadioButton) findViewById(R.id.set_align_right);
		settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);
	}
	
	private void setAttri() {
        String col = settings.getString(SET_THEAM, "O");
		if(col.equals("O"))
			rb_th_OW.setChecked(true);
		else if(col.equals("B"))
			rb_th_BW.setChecked(true);

		for(int i=0;i<tv_ui_text.length;i++){
			if(col.equals("O"))
				tv_ui_text[i].setTextColor(getResources().getColor(R.color.Orange));
			else if(col.equals("B"))
				tv_ui_text[i].setTextColor(getResources().getColor(R.color.Blue));
		}
		for(int i=0;i<tv_ui_line.length;i++){
			if(col.equals("O"))
				tv_ui_line[i].setBackgroundColor(getResources().getColor(R.color.Orange));
			else if(col.equals("B"))
				tv_ui_line[i].setBackgroundColor(getResources().getColor(R.color.Blue));
		}

		//TEXT FORMATING
		int vtf = settings.getInt(SET_TEXT_FORMATING, 1);
		if(vtf==0)
			sw_voice.setChecked(false);
		else
			sw_voice.setChecked(true);
		
		//DEMO
		int dm = settings.getInt(SET_DEMO, 1);
		if(dm==0)
			sw_demo.setChecked(false);
		else
			sw_demo.setChecked(true);
			
		/////////ORIENTATION
		String ori = settings.getString(SET_ORI, "P");
		if(ori.equals("L")){
			rb_ori_land.setChecked(true);
			ori_LandScape();
		}else if(ori.equals("P")){
			rb_ori_port.setChecked(true);
			ori_Portrait();
		}else if(ori.equals("A")){
			rb_ori_auto.setChecked(true);
			ori_Auto();
		}
		
		////FONT
		int f = settings.getInt(SET_FONT, 1);
		if(f==1){
			rb_fonts_1.setChecked(true);
		}else if(f==2){
			rb_fonts_2.setChecked(true);
		}else if(f==3){
			rb_fonts_3.setChecked(true);
		}else if(f==4){
			rb_fonts_4.setChecked(true);
		}else if(f==5){
			rb_fonts_5.setChecked(true);
		}
		
		//Alignment
		String al = settings.getString(SET_ALIGNMENT, "L");
		if(al.equals("L")){
			rb_align_l.setChecked(true);
		}else if(al.equals("C")){
			rb_align_c.setChecked(true);
		}else if(al.equals("R")){
			rb_align_r.setChecked(true);
		}
		
		
		////SIZE
		int sz = settings.getInt(SIZE_OF_TEXT, 20);
		sb_size.setProgress(sz);
		tv_seekbar.setText(""+sz);
		
		//FULLSCREEN
		int fs = settings.getInt(SET_FULLSCREEN, 0);
		if(fs==1){
			sw_fullScreen.setChecked(true);
			//requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			sw_fullScreen.setChecked(false);
		}
	}
	
    private void ori_LandScape(){
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    private void ori_Portrait(){
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);    	
    }
    private void ori_Auto(){
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

	
	private void SETTheams() {
		rb_th_OW.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_th_BW.setChecked(false);
				//settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_THEAM, "O");
			    editor.commit();
			    setAttri();
			}
		});
		rb_th_BW.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_th_OW.setChecked(false);
				//settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_THEAM, "B");
			    editor.commit();
			    setAttri();
			}
		});
	}
	
	private void SETOri() {
		rb_ori_land.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_ori_auto.setChecked(false);
				rb_ori_port.setChecked(false);
		        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_ORI, "L");
			    editor.commit();
			    setAttri();
			}
		});
		rb_ori_port.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_ori_land.setChecked(false);
				rb_ori_auto.setChecked(false);
		        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_ORI, "P");
			    editor.commit();
			    setAttri();
			}
		});
		rb_ori_auto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_ori_land.setChecked(false);
				rb_ori_port.setChecked(false);
		        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_ORI, "A");
			    editor.commit();
			    setAttri();
			}
		});
	}
	
	private void SETTextFormating() {
		sw_voice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked==true){
			        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
				    SharedPreferences.Editor editor = settings.edit();
				    editor.putInt(SET_TEXT_FORMATING, 1);
				    editor.commit();
				}else{
			        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
				    SharedPreferences.Editor editor = settings.edit();
				    editor.putInt(SET_TEXT_FORMATING, 0);
				    editor.commit();
				}
				//Toast.makeText(getApplicationContext(), ""+settings.getInt(SET_TEXT_FORMATING, 1), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	private void init_Font() {
		Typeface tf1 = Typeface.createFromAsset(this.getAssets(), "fonts/centabel.ttf");
		Typeface tf2 = Typeface.createFromAsset(this.getAssets(), "fonts/HaloHandletter.otf");
        Typeface tf3 = Typeface.createFromAsset(this.getAssets(), "fonts/MonospaceTypewriter.ttf");
        Typeface tf4 = Typeface.createFromAsset(this.getAssets(), "fonts/NeutonRegular.ttf");
        
        rb_fonts_1.setTypeface(Typeface.DEFAULT);
        rb_fonts_2.setTypeface(tf1);
        rb_fonts_3.setTypeface(tf2);
        rb_fonts_4.setTypeface(tf3);
        rb_fonts_5.setTypeface(tf4);
    	
	}
	private void SETFont(){
		//settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
	    final SharedPreferences.Editor editor = settings.edit();
		rb_fonts_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_fonts_2.setChecked(false);rb_fonts_3.setChecked(false);rb_fonts_4.setChecked(false);
				editor.putInt(SET_FONT, 1);
				rb_fonts_5.setChecked(false);
			    editor.commit();
			}
		});
		rb_fonts_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_fonts_1.setChecked(false);rb_fonts_3.setChecked(false);rb_fonts_4.setChecked(false);
				editor.putInt(SET_FONT, 2);
				rb_fonts_5.setChecked(false);
			    editor.commit();
			}
		});
		rb_fonts_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_fonts_2.setChecked(false);rb_fonts_1.setChecked(false);rb_fonts_4.setChecked(false);
				editor.putInt(SET_FONT, 3);
				rb_fonts_5.setChecked(false);
			    editor.commit();
			}
		});
		rb_fonts_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_fonts_2.setChecked(false);rb_fonts_3.setChecked(false);rb_fonts_1.setChecked(false);
				rb_fonts_5.setChecked(false);
				editor.putInt(SET_FONT, 4);
			    editor.commit();
			}
		});
		rb_fonts_5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_fonts_2.setChecked(false);rb_fonts_3.setChecked(false);rb_fonts_1.setChecked(false);
				rb_fonts_4.setChecked(false);
				editor.putInt(SET_FONT, 4);
			    editor.commit();
			}
		});
	}
	
	private void SETSize(){
		
		sb_size.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			
				sb_size.setProgress(progress);
				tv_seekbar.setText(""+progress);
				//settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);
				SharedPreferences.Editor ed = settings.edit();
				ed.putInt(SIZE_OF_TEXT, progress);
				ed.commit();
				//Toast.makeText(getApplicationContext(),""+ settings.getInt(SIZE_OF_TEXT, 89), 0).show();	
			}
		});
		
	}
	
	private void SETFullScreen(){
		sw_fullScreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor ed = settings.edit();
				if(isChecked == true){
					ed.putInt(SET_FULLSCREEN, 1);
					ed.commit();
				}else{
					ed.putInt(SET_FULLSCREEN, 0);
					ed.commit();
				}
				setAttri();
			}
		});
	}
	private void SETDemo() {
		sw_demo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor ed = settings.edit();
				if(isChecked == true){
					ed.putInt(SET_DEMO, 1);
					ed.commit();
				}else{
					ed.putInt(SET_DEMO, 0);
					ed.commit();
				}
				setAttri();
			}
		});
	}
	
	
	private void SETAlignment() {
		rb_align_l.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_align_c.setChecked(false);
				rb_align_r.setChecked(false);
		        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_ALIGNMENT, "L");
			    editor.commit();
			}
		});
		rb_align_c.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_align_l.setChecked(false);
				rb_align_r.setChecked(false);
		        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_ALIGNMENT, "C");
			    editor.commit();
			}
		});
		rb_align_r.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rb_align_l.setChecked(false);
				rb_align_c.setChecked(false);
		        //settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString(SET_ALIGNMENT, "R");
			    editor.commit();
			 }
		});
	}
	
	@Override
	protected void onPause() {
		//animate transition
		overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
		super.onPause();
	}
	@Override
	protected void onResume() {
		setAttri();
		super.onResume();
	}

}
