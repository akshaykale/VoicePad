package com.theark.notepadsq;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class Startup extends Activity {
	
	ImageView iv_open,iv_new,iv_help;
	SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FULLScreen();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
		setContentView(R.layout.startup_screen);
		
		init_TB();
		init();
		ImplictIntentManager();
		//anim();
		setBounceAnim(iv_help);
		setBounceAnim(iv_open);
		setBounceAnim(iv_new);
		
		final Intent newIntent = new Intent(Startup.this,MainActivity.class);
		iv_open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newIntent.putExtra("startup", 0);//for open
				startActivity(newIntent);
				//Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
			}
		});
		iv_new.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newIntent.putExtra("startup", 1);//for new
				startActivity(newIntent);
				//animate transition
				overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
				//Toast.makeText(getApplicationContext(), "new", Toast.LENGTH_SHORT).show();
			}
		});
		iv_help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newIntent.putExtra("startup", 2);//for help
				startActivity(newIntent);
				//Toast.makeText(getApplicationContext(), "help", Toast.LENGTH_SHORT).show();
			}
		});
		findViewById(R.id.textView1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				anim();
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
    
    private void ImplictIntentManager() {
    	String uri ="";
    	Uri uri2;
    	//final Intent intent = getIntent();  
        final String action = getIntent().getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            //uri = intent.getStringExtra("URI");
            uri2 = getIntent().getData();
            uri = uri2.getEncodedPath();
            Intent intentIMP = new Intent(Startup.this,MainActivity.class);
            intentIMP.putExtra("startup", 1);//to open MainActivity
            intentIMP.putExtra("Implicit_file_path", uri2.toString());
            intentIMP.putExtra("Implicit_file_path_confirmation", 1);
            
            startActivity(intentIMP);
            Toast.makeText(getApplicationContext(), uri, 1).show();
        }
	}
	
    //BOUNCE ANIM OF BUTTONS
	private void setBounceAnim(View view){
		//
		Animation anim = AnimationUtils.loadAnimation(Startup.this, R.anim.push_down_in_out);
		view.setAnimation(anim);
		anim.start();
		//
	}
	
	//LAYOUT ANIM
	private void anim() {
		Animation anim = AnimationUtils.loadAnimation(Startup.this, R.anim.push_down_in_out);
		findViewById(R.id.startup_lin_lay).setAnimation(anim);
		//iv_new.setAnimation(anim);
		anim.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId())
		{
		case R.id.action_startup_close:
			System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("NewApi")
	private void init_TB() {
		this.setTitleColor(Color.parseColor("#000000"));
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.cll__));//"#99AF9C91"
		getActionBar()/* or getSupportActionBar() */
		.setTitle(Html.fromHtml("<font color=\"white\">" + getString(R.string.app_name) + "</font>"));
	}
	
	
	private void init() {
		iv_help = (ImageView) findViewById(R.id.imageView3);
		iv_new = (ImageView) findViewById(R.id.imageView2);
		iv_open = (ImageView) findViewById(R.id.imageView1);
	}
	
	
	
	
	
	@Override
	protected void onResume() {
		FULLScreen();
		super.onResume();
	}
	
}
