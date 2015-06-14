package com.theark.notepadsq;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

public class DemoActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.demo_activity);
/*		final int []imageArray={R.drawable.cll___,R.drawable.cll,R.drawable.cl_};


		final Handler handler = new Handler();
		         Runnable runnable = new Runnable() {
		            int i=0;
		            public void run() {
		                findViewById(R.id.iv_demo).setBackgroundResource(imageArray[i]);
		                i++;
		                if(i>imageArray.length-1)
		                {
		                i=0;    
		                }
		                handler.postDelayed(this, 4000);  //for interval...
		            }
		        };
		        handler.postDelayed(runnable,0); //for initial delay..
	}*/
		autoSmoothScroll();

	}
	
	public void autoSmoothScroll() {

	    final HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.hv_demo);
	    hsv.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	            //hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
	            hsv.smoothScrollBy(50, 0);
	        }
	    },10);
	}
}

