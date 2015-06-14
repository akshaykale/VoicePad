package com.theark.notepadsq;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class HelpActivity extends Activity {
	
	private static final int REQ_CODE_HELP_COMMAND_INPUT = 123;
	private ListView lv_list,lv_cmd_list;
	private MyAdapter_Help myHelpAdapter;
	private MyAdapter_CMD myCMDAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.help_activity_ui);
		//FULLScreen();
		init_TB();
		
		lv_list = (ListView) findViewById(R.id.help_act_ui_lv);
		myHelpAdapter = new MyAdapter_Help(this);
		lv_list.setAdapter(myHelpAdapter);

		setAttri();
        lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				onItemClickRightDrawer(pos);
			}	
		});
	}
	
	//FULLSCREEN
    private void FULLScreen() {
    	SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
		int fs = settings.getInt(SettingsActivity.SET_FULLSCREEN, 0);
		if(fs==1){
			//requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
		}
	}

	@SuppressLint("NewApi")
	private void init_TB() {//title bar setting
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EBEBEB")));
		bar
		.setTitle(Html.fromHtml("<font color=\"#564F47\">" + getString(R.string.app_name) + "</font>"));
	}
	
	private void setAttri() {

	}


	protected void onItemClickRightDrawer(int pos) {
		switch(pos){
		case 0:
			Demo();break;
		case 1:
			CommandsHelp();break;
		case 2:
			Feedback();break;
		case 3:
			Rate();break;
		case 4:
			break;
		case 5:
			break;
		}
	}

    private void Demo() {
		//startActivity(new Intent(HelpActivity.this,DemoActivity.class));
    	Toast.makeText(getApplicationContext(), "Intro Clip", Toast.LENGTH_SHORT).show();
    }

	//Google Voice Recognizer Intent/////////////////////////////////
    private void speech_to_text(int code) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, code);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "not supported",
                    Toast.LENGTH_SHORT).show();
        }
	}
    //Google Voice Recognizer Intent/////////////////////////////////
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    	
        switch (requestCode) {                       
        //for Command Input
        case REQ_CODE_HELP_COMMAND_INPUT:{
            if (resultCode == RESULT_OK && null != data) {
            	 
                ArrayList<String> result_cmd = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(this,result_cmd.get(0) , Toast.LENGTH_SHORT).show();
                String mic_Commands=result_cmd.get(0);
                
                if(mic_Commands.equals("voice commands")||
                		mic_Commands.equals("recognisable commands")){
                	CommandsHelp();
                }else if(mic_Commands.equals("feedback")||
                		mic_Commands.equals("back")){
                	Feedback();
                }else if(mic_Commands.equals("rate")|| mic_Commands.equals("hey")||
                		mic_Commands.equals("meet")|| mic_Commands.equals("late")){
                	Rate();
                }else if(mic_Commands.equals("developer regference")){
                	DevRef();
                }else if(mic_Commands.equals("print") || mic_Commands.equals("please")){
                //	Command_Print();
                }else if(mic_Commands.equals("settings") || mic_Commands.equals("sleeping")){
                //	Command_Settings();
                }else if(mic_Commands.equals("exit")){
                //	Command_Exit();
                }else if(mic_Commands.equals("demo") || mic_Commands.equals("babel")){
                	Demo();
                }else if(mic_Commands.equals("edit")){
                	
                }       
            }
            break;
        }
        }
    }

	private void DevRef() {
		
	}


	private void Rate() {
		final Dialog dialog = new Dialog(HelpActivity.this);		
		
		dialog.setContentView(R.layout.help_rateus);
        dialog.setTitle("Rate Us");
        dialog.setCancelable(true);
		
        Button bt_rate_save = (Button) dialog.findViewById(R.id.help_rate_bt);
        
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
        String col = settings.getString(SettingsActivity.SET_THEAM, "O");
		if(col.equals("O")){
			bt_rate_save.setBackgroundColor(getResources().getColor(R.color.Orange));
		}else if(col.equals("B")){
			bt_rate_save.setBackgroundColor(getResources().getColor(R.color.Blue));
		}
        
        //Button bt_cmd_close = (Button) dialog.findViewById(R.id.help_cmd_diad_close);
        bt_rate_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
	}

	private void Feedback() {
		final Dialog dialog = new Dialog(HelpActivity.this);		
		
		dialog.setContentView(R.layout.help_feedback);
        dialog.setTitle("Email Feedback");
        //dialog.setCancelable(false);
		
        Button bt_fb_send = (Button) dialog.findViewById(R.id.bt_help_fb_send);
        final EditText et_msg = (EditText) dialog.findViewById(R.id.et_help_fb_msg);
        //Button bt_cmd_close = (Button) dialog.findViewById(R.id.help_cmd_diad_close);
        
        TextView tv1,tv2;
        tv1 = (TextView) dialog.findViewById(R.id.help__fb_MainTitle);
        tv2 = (TextView) dialog.findViewById(R.id.help_fb_seperator);
        
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
        String col = settings.getString(SettingsActivity.SET_THEAM, "O");
		if(col.equals("O")){
			tv1.setTextColor(getResources().getColor(R.color.Orange));
			tv2.setBackgroundColor(getResources().getColor(R.color.Orange));
			bt_fb_send.setBackgroundColor(getResources().getColor(R.color.Orange));
		}else if(col.equals("B")){
			tv1.setTextColor(getResources().getColor(R.color.Blue));
			tv2.setBackgroundColor(getResources().getColor(R.color.Blue));
			bt_fb_send.setBackgroundColor(getResources().getColor(R.color.Blue));
		}
        
        bt_fb_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_msg.getText().toString();
                String sub = "Notepad3+ Feedback";
                String[] to = {"theaak1204@gmail.com"};
                
            	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, to);
            	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, sub);
            	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
            	emailIntent.setType("plain/text");
            	startActivity(emailIntent);
            	
            	dialog.dismiss();
            }
        });
        dialog.show();
	}


	private void CommandsHelp() {
		final Dialog dialog = new Dialog(HelpActivity.this);		
		
		dialog.setContentView(R.layout.help_commands_dialog);
        dialog.setTitle("Commands Help");
        dialog.setCancelable(true);
		lv_cmd_list = (ListView) dialog.findViewById(R.id.help_cmd_list);
		myCMDAdapter = new MyAdapter_CMD(this);
		lv_cmd_list.setAdapter(myCMDAdapter);

       // Button bt_sd_save = (Button) dialog.findViewById(R.id.bt_dialog_save_save);
        Button bt_cmd_close = (Button) dialog.findViewById(R.id.help_cmd_diad_close);
        
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
        String col = settings.getString(SettingsActivity.SET_THEAM, "O");
		if(col.equals("O")){
			bt_cmd_close.setBackgroundColor(getResources().getColor(R.color.Orange));
			//bt_sd_save.setTextColor(getResources().getColor(R.color.Orange));
		}else if(col.equals("B")){
			bt_cmd_close.setBackgroundColor(getResources().getColor(R.color.Blue));
			//bt_sd_save.setTextColor(getResources().getColor(R.color.Blue));
		}
        bt_cmd_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu._helpmain, menu);
        return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_speek_command:
			speech_to_text(REQ_CODE_HELP_COMMAND_INPUT);
			break;

		default:
			break;
		}	
		return super.onOptionsItemSelected(item);
	}

////////////////////////////////////////////////////////////
	private class MyAdapter_Help extends BaseAdapter {
		Context context;
		String[] help_items_title,help_items_disL,help_items_disS;

		public MyAdapter_Help(Context context) {
			this.context=context;
			help_items_title = context.getResources().getStringArray(R.array.help_mainTitle);
			help_items_disL = context.getResources().getStringArray(R.array.help_disL);
			help_items_disS = context.getResources().getStringArray(R.array.help_disS);
		}
		@Override
		public int getCount() {
			return help_items_title.length;
		}
		@Override
		public Object getItem(int pos) {
			return help_items_title[pos];
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
				row = inflater.inflate(R.layout.help_activity, parent,false);
			}else{
				row = convertView;
			}
			//create java obj and connect to xml obj
			TextView tv_htitle,tv_hdisL,tv_hdisS,tv_line;
			tv_htitle = (TextView) row.findViewById(R.id.help_MainTitle);
			tv_hdisL = (TextView) row.findViewById(R.id.help_DiscrpL);
			tv_hdisS = (TextView) row.findViewById(R.id.help_DiscrpS);
			tv_line = (TextView) row.findViewById(R.id.help_seperator);
			
			SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
	        String col = settings.getString(SettingsActivity.SET_THEAM, "O");
			if(col.equals("O")){
				tv_htitle.setTextColor(getResources().getColor(R.color.Orange));
				tv_line.setBackgroundColor(getResources().getColor(R.color.Orange));
			}else if(col.equals("B")){
				tv_htitle.setTextColor(getResources().getColor(R.color.Blue));
				tv_line.setBackgroundColor(getResources().getColor(R.color.Blue));
			}
			
			tv_htitle.setText(help_items_title[position]);
			tv_hdisL.setText(help_items_disL[position]);
			tv_hdisS.setText(help_items_disS[position]);
			
			
			//return the row view
			return row;
		}
	}

	private class MyAdapter_CMD extends BaseAdapter {
		Context context1;
		String[] help_cmd_title,help_cmd_dis;

		public MyAdapter_CMD(Context context_) {
			this.context1=context_;
			help_cmd_title = context_.getResources().getStringArray(R.array.cmds_title);
			help_cmd_dis = context_.getResources().getStringArray(R.array.cmds_dis);
		}
		@Override
		public int getCount() {
			return help_cmd_title.length;
		}

		@Override
		public Object getItem(int pos) {
			return help_cmd_title[pos];
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
				LayoutInflater inflater = (LayoutInflater) context1.
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.help_activity_commands, parent,false);
			}else{
				row = convertView;
			}
			//create java obj and connect to xml obj
			TextView tv_htitle,tv_hdis,tv;
			tv_htitle = (TextView) row.findViewById(R.id.help_MainTitle);
			tv_hdis = (TextView) row.findViewById(R.id.help_DiscrpL);
			tv = (TextView) row.findViewById(R.id.help_seperator);
			SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, MODE_WORLD_WRITEABLE);  
	        String col = settings.getString(SettingsActivity.SET_THEAM, "O");
			if(col.equals("O")){
				tv_htitle.setTextColor(getResources().getColor(R.color.Orange));
				tv.setBackgroundColor(getResources().getColor(R.color.Orange));
			}else if(col.equals("B")){
				tv_htitle.setTextColor(getResources().getColor(R.color.Blue));
				tv.setBackgroundColor(getResources().getColor(R.color.Blue));
			}
			
			tv_htitle.setText(help_cmd_title[position]);
			tv_hdis.setText(help_cmd_dis[position]);
			//return the row view
			return row;
		}
	}
	
	@Override
	protected void onPause() {
		//animate transition
		overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
		super.onPause();
	}
	@Override
	protected void onResume() {
		FULLScreen();
		super.onResume();
	}
}
