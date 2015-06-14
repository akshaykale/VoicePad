package com.theark.notepadsq;

import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.InputType;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
//import net.bgreco.DirectoryPicker;
//import com.coderplus.filepicker.FilePickerActivity;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnItemClickListener{
	
	String applink = "";
	
    public String[] drawer_file_menu_names_array;
    public String[] drawer_edit_menu_names_array;
    public String backup_text;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList_left;
    private ListView mDrawerList_right;
    private ActionBarDrawerToggle drawerListener;
    //text
    private Button bt_speek_text;
    private EditText et_text;
    //pdf
    private Button bt_pdf_speek_text,bt_pdf_speek_command;
    private EditText et_pdf_textinput;
    
    static final int REQUEST_PICK_FILE = 777;
	public static final String PREFS_NAME = "funct";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int REQ_CODE_COMMAND_INPUT = 110;
    private final int REQ_CODE_EDIT_COMMAND_INPUT = 120;
    private final int REQ_CODE_SEARCH_DIALOG_INPUT = 130;
    private final int DIR_PICK_SAVE = 555;
    private final int DIR_PICK_OPEN = 553;
    
    TextToSpeech ttobj;
    
    private MyAdapter myAdapter_left;
    private MyAdapter_edit myAdapter_right;
    private InputMethodManager im ;
    
    public String FUNCTION="save";
    SharedPreferences settings ;
    ///edit menu
    String selectedText_;
    int startSelection,endSelection;
    
    private ClipboardManager myClipboard;
    private ClipData myClip;

    ///dialog variable
    String save_path;//save dialog
    String strTemp_search_dialog_kw=null;
    
    //ui TVs
    private TextView ui_tv_3,ui_tv_01,ui_tv_02;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        FULLScreen();
        this .getWindow().setSoftInputMode(WindowManager.LayoutParams. SOFT_INPUT_STATE_ALWAYS_HIDDEN );
        setContentView(R.layout.activity_main_pdf);
        
        init();
        
        init_TB();
        //hide_keybord();
        setAttri();
        
        ImplictIntentManager();
        
        //manage the startup activity
        startupIntentManager();
        
        ttobj=new TextToSpeech(getApplicationContext(), 
	      new TextToSpeech.OnInitListener() {
	      @Override
	      public void onInit(int status) {
	         if(status != TextToSpeech.ERROR){
	             ttobj.setLanguage(Locale.UK);
	            }				
	         }
	     });
        
      //Set the adapter for the list view
        //left FILE MENU
        myAdapter_left = new MyAdapter(this);//custom row
        mDrawerList_left.setAdapter(myAdapter_left);
        //right EDIT MENU
        myAdapter_right = new MyAdapter_edit(this);//custom row
        mDrawerList_right.setAdapter(myAdapter_right);
        
      //File/Drawer Menu Click Events
        mDrawerList_left.setOnItemClickListener((OnItemClickListener) this);
        mDrawerList_right.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				onItemClickRightDrawer(pos);
			}
		});
        
      //Closing and Opening the Drawer///////////////////////////////////////    
        drawerListener = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_bar,
        					R.string.drawer_open,R.string.drawer_close){
        	@Override
        	public void onDrawerClosed(View drawerView) {
        		init_TB();
        		//Toast.makeText(getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
        		//super.onDrawerClosed(drawerView);
        	}
        	
        	@Override
        	public void onDrawerOpened(View drawerView) {
        		//Toast.makeText(getApplicationContext(), "Opened", Toast.LENGTH_SHORT).show();
     			//super.onDrawerOpened(drawerView);
    		}
        };
        mDrawerLayout.setDrawerListener(drawerListener);//add the listener
      //Closing and Opening the Drawer///////////////////////////////////////
        
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
      //click listener on bt_speek//////////////////////////////////////// 
        bt_pdf_speek_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        //im.showSoftInput(et_text, InputMethodManager.SHOW_IMPLICIT);
				speech_to_text(REQ_CODE_SPEECH_INPUT);
			}
		});

      //click listener on bt_speek///////////////////////////////////////////
    }
    
    private void ImplictIntentManager() {
    	String TAG = "TagOpenTxt";
    	String uri ="";
    	Uri uri2;
    	final Intent intent = getIntent();  
        final String action = intent.getAction();

        if(Intent.ACTION_VIEW.equals(action)){
            //uri = intent.getStringExtra("URI");
            uri2 = intent.getData();
            uri = uri2.getEncodedPath() + "  complete: " + uri2.toString();
            //TextView textView = (TextView)findViewById(R.id.textView);
            //textView.setText(uri);
            //Toast.makeText(getApplicationContext(), uri, 0).show();
            // now you call whatever function your app uses 
            // to consume the txt file whose location you now know 
        } else {
            Log.d(TAG, "intent was something else: "+action);
        }
	}

	//manage startup Activity Intents
    private void startupIntentManager(){
    	int todo = getIntent().getExtras().getInt("startup");
    	//implicit file_path
    	String fp = getIntent().getExtras().getString("Implicit_file_path");
    	if(todo == 0){
    		Command_Open();
    	}else if(todo == 2){
    		Command_Help();
    	}else{
    		if(getIntent().getExtras().getInt("Implicit_file_path_confirmation")==1){
    			/*String[] pat = fp.split("/");
    			et_pdf_textinput.setText(fp+"\n\n");
    			String fPath = pat[pat.length-1];
    			String dirPath = "/";
    			for(int i=3;i<pat.length-1;i++){
    				dirPath+=pat[i]+"/";
    				
    			}
    			if(dirPath.contains("%20")){
    				dirPath = dirPath.replace("%20","");
    			}if(fPath.contains("%20")){
    				fPath = fPath.replace("%20","");
    			}*/if(fp.contains("%20")){
    				fp = fp.replace("%20","");
    			}
    			
    			//ImplicitFileOpen(dirPath,fPath);
    			ImplicitFileOpen(fp.replace("file://", ""));

    			//et_pdf_textinput.setText(dirPath);
    			//et_pdf_textinput.append("\n\n"+fp);
    			//Toast.makeText(getApplicationContext(),"fileN:"+fPath, 1).show();
    			//Toast.makeText(getApplicationContext(),"dirN:"+dirPath, 1).show();
    		}else{
    			//normal
    		}
    	}
    }
    
    //private void ImplicitFileOpen(String dirPath,String fileName){
    private void ImplicitFileOpen(String uri){
    		
		BufferedReader bufferedReader=null;
		StringBuilder result = new StringBuilder();

			try {	
	  		//settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
	  		//File open_file = new File(dirPath,fileName);
	  		File of = new File(uri);  
			//InputStream fileInputStream = openFileInput(settings.getString("file_name_to_open", "aa"));
			//bufferedReader = new BufferedReader(new FileReader(open_file));
			bufferedReader = new BufferedReader(new FileReader(of));
			String data_line;

			while((data_line = bufferedReader.readLine())!=null){
					//result.append(data_line +"\r\n");
				result.append(data_line);
				result.append("\n");
				}
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
			et_pdf_textinput.setText(result);
    }
    
    @SuppressLint("NewApi")
	private void init_TB() {//title bar setting
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.cll__));
		bar
		.setTitle(Html.fromHtml("<font color=\"white\">" + getString(R.string.app_name) + "</font>"));
	}
    
    //Initialize the Variables///////////////////////////////////////
    private void init() {
    	//setAttri
    	ui_tv_3 = (TextView) findViewById(R.id.textView3);
    	ui_tv_01 = (TextView) findViewById(R.id.TextView01);
    	ui_tv_02 = (TextView) findViewById(R.id.TextView02);
    	//array
        drawer_file_menu_names_array = getResources().getStringArray(R.array.file_menu);
        drawer_edit_menu_names_array = getResources().getStringArray(R.array.edit_menu);
        //lists and drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList_left = (ListView) findViewById(R.id.left_drawer);
        mDrawerList_right = (ListView) findViewById(R.id.right_drawer);
        //main UI text
        bt_speek_text = (Button) findViewById(R.id.bt_dialog_save_cancle);
        et_text = (EditText) findViewById(R.id.et_dialog_save_filename);
		//pdf
        bt_pdf_speek_text = (Button) findViewById(R.id.pdf_bt_speek_text);
        et_pdf_textinput = (EditText) findViewById(R.id.pdf_et_input);
        et_pdf_textinput.setSingleLine(false);
        


       // et_pdf_textinput.set
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        
        settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);  
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("func", "open");
	      editor.commit();
	}
    //Initialize the Variables///////////////////////////////////////
    
    
    private void setAttri(){
        settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);  
	    
        ////////Ori
		String ori = settings.getString(SettingsActivity.SET_ORI, "P");
		if(ori.equals("L")){
			ori_LandScape();
		}else if(ori.equals("P")){
			ori_Portrait();
		}else if(ori.equals("A")){
			ori_Auto();
		}
		//SET_FONT
		int font = settings.getInt(SettingsActivity.SET_FONT, 1);
		String[] fontName = {" "," ","fonts/centabel.ttf","fonts/HaloHandletter.otf",
				"fonts/MonospaceTypewriter.ttf","fonts/NeutonRegular.ttf"};
		if(font==1){
			et_pdf_textinput.setTypeface(Typeface.DEFAULT);
		}else{
			Typeface tf = Typeface.createFromAsset(this.getAssets(), fontName[font]);
			et_pdf_textinput.setTypeface(tf);
		}
		///////SIZE
		int sz = settings.getInt(SettingsActivity.SIZE_OF_TEXT, 20);
		et_pdf_textinput.setTextSize((float)sz);
		
		//Alignment
		String ali = settings.getString(SettingsActivity.SET_ALIGNMENT, "L");
		if(ali.equals("L")){
			et_pdf_textinput.setGravity(Gravity.LEFT);
		}else if(ali.equals("C")){
			et_pdf_textinput.setGravity(Gravity.CENTER_HORIZONTAL);
		}else if(ali.equals("R")){
			et_pdf_textinput.setGravity(Gravity.RIGHT);
		}
		
		
	    ////////THEAM
        ActionBar bar = getActionBar();
		String col = settings.getString(SettingsActivity.SET_THEAM, "O");
			if(col.equals("O")){
		        //setTitleColor(getResources().getColor(R.color.White));
		        //bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Orange)));
				ui_tv_02.setBackgroundColor(getResources().getColor(R.color.Orange));
				ui_tv_3.setBackgroundColor(getResources().getColor(R.color.Orange));
				ui_tv_01.setTextColor(getResources().getColor(R.color.Orange));
				//bt_pdf_speek_text.setBackgroundColor(getResources().getColor(R.color.Orange));
			}else if(col.equals("B")){
		        //setTitleColor(getResources().getColor(R.color.White));
		        //bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Blue)));
				ui_tv_02.setBackgroundColor(getResources().getColor(R.color.Blue));
				ui_tv_3.setBackgroundColor(getResources().getColor(R.color.Blue));
				ui_tv_01.setTextColor(getResources().getColor(R.color.Blue));
				//bt_pdf_speek_text.setBackgroundColor(getResources().getColor(R.color.Blue));
			}
			//voice text formating
		      et_pdf_textinput.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
		            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		        		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==1){
		        			startSelection=et_pdf_textinput.getSelectionStart();
		            		endSelection=et_pdf_textinput.getSelectionEnd();
		            		selectedText_ = et_pdf_textinput.getText().toString().
		            				substring(startSelection, endSelection);
		                	speech_to_text(REQ_CODE_COMMAND_INPUT);
		                	//Toast.makeText(getApplicationContext(), selectedText_, 0).show();
		        		}
		            	return true;
		            }public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
		                return true;
		            }public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
		                return true;
		            }public void onDestroyActionMode(ActionMode actionMode) {}
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
    
    
    

    //drawer Menu Settings///////////////////////////////
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
       	super.onPostCreate(savedInstanceState);
       	drawerListener.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	drawerListener.onConfigurationChanged(newConfig);
    }
    //drawer Menu Settings///////////////////////////////    
    
    
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
    private void text_to_speech(){
		int startSelection=et_pdf_textinput.getSelectionStart();
		int endSelection=et_pdf_textinput.getSelectionEnd();

		String selectedText = et_pdf_textinput.getText().toString().
				substring(startSelection, endSelection);
		String toSpeak;
		if(selectedText.equals("")){
	        toSpeak = et_pdf_textinput.getText().toString();
		}else{
			toSpeak = selectedText;
		}
		
        //Toast.makeText(getApplicationContext(), toSpeak, 
        //Toast.LENGTH_SHORT).show();
        ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
    //Google Voice Recognizer Intent/////////////////////////////////
    

    
    /////////hide Keybord/////////////
    private void hide_keybord(){
    	et_pdf_textinput.setInputType(InputType.TYPE_NULL);      
    	if (android.os.Build.VERSION.SDK_INT >= 11)   
    	{  
    		et_pdf_textinput.setRawInputType(InputType.TYPE_CLASS_TEXT);  
    		et_pdf_textinput.setTextIsSelectable(true);  
    	}
    }
    private void unhide_keybord(){
    	et_pdf_textinput.setInputType(InputType.TYPE_CLASS_TEXT);
        et_pdf_textinput.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(et_pdf_textinput, InputMethodManager.SHOW_FORCED);
    	et_pdf_textinput.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
    	//et_pdf_textinput.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }
    /////////hide Keybord/////////////
    private void ori_LandScape(){
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    private void ori_Portrait(){
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);    	
    }
    private void ori_Auto(){
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
    

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(drawerListener.onOptionsItemSelected(item)){
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_speek_command:
			speech_to_text(REQ_CODE_COMMAND_INPUT);
			break;
		case R.id.action_pdf:
			if(ttobj.isSpeaking()){
				ttobj.stop();
			}else
				text_to_speech();

		default:
			break;
		}	
		return super.onOptionsItemSelected(item);
	}


	//Drawer Items Click Event////////////////////////////////////
	//Left
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		switch(pos){

		case 0:
			Command_NewDocument();
			mDrawerLayout.closeDrawers();
			break;
		case 1:
			Command_Open();
			mDrawerLayout.closeDrawers();
			break;
		case 2:
			Command_Save();
			mDrawerLayout.closeDrawers();
			break;
		case 3:
			//Command_Print();
			Command_Settings();
			mDrawerLayout.closeDrawers();
			break;
		case 4:
			//Command_Settings();
			Command_Help();
			mDrawerLayout.closeDrawers();
			break;
		case 5:
			//Command_Help();
			Command_Share();
			mDrawerLayout.closeDrawers();
			break;
		case 6:
			Command_Exit();
			mDrawerLayout.closeDrawers();
			break;
		}
	}
	//Right
	private void onItemClickRightDrawer(int pos){
		switch(pos){
		case 0:
			speech_to_text(REQ_CODE_COMMAND_INPUT);
			mDrawerLayout.closeDrawers();
			break;
		case 1:
			Command_Cut();
			mDrawerLayout.closeDrawers();
			break;
		case 2:
			Command_Copy();
			mDrawerLayout.closeDrawers();
			break;
		case 3:
			Command_Paste();
			mDrawerLayout.closeDrawers();
			break;
		case 4:
			Command_SelectAll();
			mDrawerLayout.closeDrawers();
			break;
		case 5:
			Command_Delete();
			mDrawerLayout.closeDrawers();
			break;
		case 7:
			Command_Font();
			mDrawerLayout.closeDrawers();
			break;
		case 8:
			Command_Size();
			mDrawerLayout.closeDrawers();
			break;
		case 6:
			Search_Word();
			mDrawerLayout.closeDrawers();
			break;
		case 9:
			Command_Bold();
			mDrawerLayout.closeDrawers();
			break;
		case 10:
			Command_Italic();
			mDrawerLayout.closeDrawers();
			break;
		case 11:
			Command_Underline();
			mDrawerLayout.closeDrawers();
			break;
		
		}
	}
	//Drawer Items Click Event////////////////////////////////////
	
	
    //////////////////////////////////////////////////////////////////////
    //Voice Main Task...IN ACTION..///////////////////////////////////////
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        ////////OPEN FM
    	if(requestCode == DIR_PICK_OPEN && resultCode == RESULT_OK) {
    		Bundle extras = data.getExtras();
    		String open_dir_path = "";
    		open_dir_path = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
    		Open_File(open_dir_path);
    		    		
    	}
        ////////SAVE FM
    	if(requestCode == DIR_PICK_SAVE && resultCode == RESULT_OK) {
    		Bundle extras = data.getExtras();
    		String path = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
    		save_path=path;
            //Toast.makeText(this,save_path , Toast.LENGTH_SHORT).show();

    	}
        //////////////
    	
        switch (requestCode) {
                       
        //for editing input
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                et_pdf_textinput.append(" "+result.get(0));
            }
            break;
        }
        //for Command Input
        case REQ_CODE_COMMAND_INPUT:{
            if (resultCode == RESULT_OK && null != data) {
            	 
                ArrayList<String> result_cmd = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(this,result_cmd.get(0) , Toast.LENGTH_SHORT).show();
                String mic_Commands=result_cmd.get(0);
                
                if(mic_Commands.equals("clear")|| mic_Commands.equals("leo")
                		|| mic_Commands.equals("year")|| mic_Commands.equals("player")){
                	et_pdf_textinput.setText("");
                }else if(mic_Commands.equals("new document")||mic_Commands.equals("new file")){
                	Command_NewDocument();
                }else if(mic_Commands.equals("open")||mic_Commands.equals("oakland")){
                	Command_Open();
                }else if(mic_Commands.equals("save")||mic_Commands.equals("there'll")){
                	Command_Save();
                }else if(mic_Commands.equals("print") || mic_Commands.equals("please")
                		|| mic_Commands.equals("brain")){
                	Command_Print();
                }else if(mic_Commands.equals("settings") || mic_Commands.equals("sleeping")){
                	Command_Settings();
                }else if(mic_Commands.equals("exit")){
                	Command_Exit();
                }else if(mic_Commands.equals("file") || mic_Commands.equals("hi")){
                	//mDrawerLayout.openDrawer(0);
                }else if(mic_Commands.equals("help") || mic_Commands.equals("hell")){
                	Command_Help();
                }else if(mic_Commands.equals("tab")||mic_Commands.equals("dad")){
                	et_pdf_textinput.append("\t");
                }else if(mic_Commands.equals("enter")|| mic_Commands.equals("new live")){
                	et_pdf_textinput.append("\n");
                }else if(mic_Commands.equals("space")){
                	et_pdf_textinput.append(" ");
                }else if(mic_Commands.contains("search") || mic_Commands.contains("find")
                		|| mic_Commands.contains("replace")){
/*                	String str = mic_Commands;
                	str = str.replace("search ", "");
                	Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
                	Command_Search_Word(str);*/
                	Search_Word();
                }else if(mic_Commands.equals("copy")||mic_Commands.equals("coffee")
                		||mic_Commands.equals("coby")){
                	Command_Copy();
                }else if(mic_Commands.equals("cut")||mic_Commands.equals("kurt")
                		||mic_Commands.equals("court")){
                	Command_Cut();
                }else if(mic_Commands.equals("paste")||mic_Commands.equals("pace")
                		||mic_Commands.equals("best")||mic_Commands.equals("test")
                		||mic_Commands.equals("face")){
                	Command_Paste();
                }else if(mic_Commands.equals("select all")||mic_Commands.equals("select on")){
                	Command_SelectAll();
                }else if(mic_Commands.equals("delete")||mic_Commands.equals("delate")){
                    	Command_Delete();
                }else if(mic_Commands.equals("exit")){
                	Command_Exit();
                }else if(mic_Commands.equals("bold")||mic_Commands.equals("gold")){
                	Command_Bold();
                }else if(mic_Commands.equals("italic")){
                	Command_Italic();
                }else if(mic_Commands.equals("underline")){
                	Command_Underline();
                }else if(mic_Commands.equals("dot")||mic_Commands.equals("full stop")){
                	et_pdf_textinput.append(".");
                }else if(mic_Commands.equals("coma")){
                	et_pdf_textinput.append(",");
                }else if(mic_Commands.equals("size")){
                	Command_Size();
                } else if(mic_Commands.equals("font")||mic_Commands.equals("porn")){
                	Command_Font();
                }else if(mic_Commands.equals("alignment")){
                	Command_Size();
                }
                
            }break;
        }

        case REQ_CODE_SEARCH_DIALOG_INPUT:
        	if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result_cmd = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                //Toast.makeText(this,result_cmd.get(0) , Toast.LENGTH_SHORT).show();
                String str_dialog_search_find,str_dialog_search_replace;
                settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);  
      	        SharedPreferences.Editor editor = settings.edit();
                if(result_cmd.get(0).contains("find")){
                	str_dialog_search_find=result_cmd.get(0).replace("find ", "");
                	editor.putString("dialog_find_keyWord",str_dialog_search_find);
          	        editor.commit();
                }else if(result_cmd.get(0).contains("replace")){
                	str_dialog_search_replace=result_cmd.get(0).replace("replace ", "");
                	editor.putString("dialog_replace_keyWord",str_dialog_search_replace);
          	        editor.commit();
                }
            }break;
        	
        }
    }
    //Voice Main Task...IN ACTION..///////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
	
	
	
	////////////////////////////////////////////////////////////////
	///////////////MENU COMMANDS/FUNCTIONS/METHODS/////////////////
	
	//new Document
	private void Command_NewDocument(){
		et_pdf_textinput.setText("");
		//Toast.makeText(this, "NewDoc", Toast.LENGTH_SHORT).show();
	}
	//Open
	private void Command_Open(){
   		Intent intent_ = new Intent(MainActivity.this, DirectoryPicker.class);
		intent_.putExtra(DirectoryPicker.START_DIR, "/storage/emulated/0");
		intent_.putExtra(DirectoryPicker.ONLY_DIRS, false);
		intent_.putExtra("function_to_do", "open");//edited//
		startActivityForResult(intent_, DIR_PICK_OPEN);
		//animate transition
		overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
		
		  settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		  Toast.makeText(getApplicationContext(),"Select File and then click on Choose",
				  Toast.LENGTH_SHORT).show();	
	}
	//Save
	private void Command_Save(){
        
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.save_dialog);
        dialog.setTitle("Save");
        final EditText et_sd_filename =  (EditText) dialog.findViewById(R.id.et_dialog_save_filename);
        et_sd_filename.setText("theark");
        //String[] ft = {".txt",".pdf",".docx"};
        final Spinner sp_sd_filetype = (Spinner) dialog.findViewById(R.id.spinner1);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageView1);
        image.setImageResource(R.drawable.ic_launcher);
        ImageButton ibt_sd_dir = (ImageButton) dialog.findViewById(R.id.imageButton1);
        Button bt_sd_save = (Button) dialog.findViewById(R.id.bt_dialog_save_save);
        Button bt_sd_close = (Button) dialog.findViewById(R.id.bt_dialog_save_cancle);
        settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);  
        String col = settings.getString(SettingsActivity.SET_THEAM, "O");        
        if(col.equals("O")){
        	et_sd_filename.setTextColor(getResources().getColor(R.color.Orange));
        	bt_sd_save.setBackgroundColor(getResources().getColor(R.color.Orange));
        	bt_sd_close.setBackgroundColor(getResources().getColor(R.color.Orange));
        }else if(col.equals("B")){
        	et_sd_filename.setTextColor(getResources().getColor(R.color.Blue));
        	bt_sd_save.setBackgroundColor(getResources().getColor(R.color.Blue));
        	bt_sd_close.setBackgroundColor(getResources().getColor(R.color.Blue));        	
        }else if(col.equals("G")){
        	et_sd_filename.setTextColor(getResources().getColor(R.color.Black));
        	bt_sd_save.setBackgroundColor(getResources().getColor(R.color.Black));
        	bt_sd_close.setBackgroundColor(getResources().getColor(R.color.Black));
        }
        bt_sd_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	String file_name = et_sd_filename.getText().toString();
            	String file_type = sp_sd_filetype.getSelectedItem().toString();
            	if(file_type.equals(".txt") || file_type.equals(".java") ||
            			file_type.equals(".cpp") || file_type.equals(".py"))
            		Save_txt_file(file_name,file_type);
            	else if(file_type.equals(".pdf"))
            		Save_pdf_file(file_name,file_type);
            	else if(file_type.equals(".docx"))
            		Save_docx_file(file_name,file_type);
                dialog.dismiss();
            }
        });
        bt_sd_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ibt_sd_dir.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
           		final Intent intent = new Intent(MainActivity.this, DirectoryPicker.class);
        		intent.putExtra(DirectoryPicker.START_DIR, "/storage/emulated/0");
        		intent.putExtra(DirectoryPicker.ONLY_DIRS, false);
        		intent.putExtra("function_to_do", "save");
        		startActivityForResult(intent, DIR_PICK_SAVE);
            }
        });
        dialog.show();

		//Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
	}
	//Print
	private void Command_Print(){
		Toast.makeText(this, "Printing Under Construction", Toast.LENGTH_SHORT).show();
	}
	//Settings
	private void Command_Settings(){
		Intent settingIntent = new Intent(MainActivity.this,SettingsActivity.class);
		startActivity(settingIntent);
		//animate transition
		overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
		//Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
	}
	private void Command_Help(){
		Intent helpIntent = new Intent(MainActivity.this,HelpActivity.class);
		startActivity(helpIntent);
		//animate transition
		overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
		//Toast.makeText(this, "Getting Help", Toast.LENGTH_SHORT).show();
	}
	//Exit
	private void Command_Exit(){
		this.finish();
		Toast.makeText(this, "Exiting", Toast.LENGTH_SHORT).show();
	}
	////////////EditMENU////////////
	//Cut
	private void Command_Cut(){
		String textToInsert="";
		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==0){//voice is enabled
			int sSelection=et_pdf_textinput.getSelectionStart();
			int eSelection=et_pdf_textinput.getSelectionEnd();
			String selectedText = et_pdf_textinput.getText().toString().
					substring(sSelection, eSelection);
			myClip = ClipData.newPlainText("text",selectedText);
			myClipboard.setPrimaryClip(myClip);
			
			int start = Math.max(et_pdf_textinput.getSelectionStart(), 0);
			int end = Math.max(et_pdf_textinput.getSelectionEnd(), 0);
			et_pdf_textinput.setText(et_pdf_textinput.getText().replace(Math.min(start, end), Math.max(start, end),
			        textToInsert, 0, textToInsert.length()));
			
		}else{//voice is disabled
			myClip = ClipData.newPlainText("text", selectedText_);
			myClipboard.setPrimaryClip(myClip);
			int start = Math.max(startSelection, 0);
			int end = Math.max(endSelection, 0);
			et_pdf_textinput.setText(et_pdf_textinput.getText().replace(Math.min(start, end), Math.max(start, end),
			        textToInsert, 0, textToInsert.length()));			
		}
		
		Toast.makeText(getApplicationContext(), "Text Cut", 
				Toast.LENGTH_SHORT).show();
	}
	///copy
	//Copy
	private void Command_Copy(){
		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==0){
			int sSelection=et_pdf_textinput.getSelectionStart();
			int eSelection=et_pdf_textinput.getSelectionEnd();
			String selectedText = et_pdf_textinput.getText().toString().
					substring(sSelection, eSelection);			
			myClip = ClipData.newPlainText("text", selectedText);
			myClipboard.setPrimaryClip(myClip);
		}else{
			myClip = ClipData.newPlainText("text", selectedText_);
			myClipboard.setPrimaryClip(myClip);			
		}
		Toast.makeText(getApplicationContext(), "Text Copied", 
				Toast.LENGTH_SHORT).show();
	}
	//Paste
	private void Command_Paste(){
	      ClipData abc = myClipboard.getPrimaryClip();
	      ClipData.Item item = abc.getItemAt(0);
			String textToInsert=item.getText().toString();
		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==1){
			int start = Math.max(startSelection, 0);
			int end = Math.max(endSelection, 0);
			et_pdf_textinput.setText(et_pdf_textinput.getText().replace(Math.max(startSelection,0), Math.max(endSelection,0),
			        textToInsert, 0, textToInsert.length()));
			//pasteField.setText(text);
		}else{
			int start = Math.max(et_pdf_textinput.getSelectionStart(), 0);
			int end = Math.max(et_pdf_textinput.getSelectionEnd(), 0);
			et_pdf_textinput.setText(et_pdf_textinput.getText().replace(Math.min(start, end), Math.max(start, end),
			        textToInsert, 0, textToInsert.length()));
		}
	      Toast.makeText(getApplicationContext(), "Text Pasted", 
	      Toast.LENGTH_SHORT).show();
	}	
	//SelectAll
	private void Command_SelectAll(){
		et_pdf_textinput.selectAll();
		//Toast.makeText(this, "SelectAll", Toast.LENGTH_SHORT).show();
	}	
	//Delete
	private void Command_Delete(){
		String textToInsert="";
		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==0){//voice is enabled
			int sSelection=et_pdf_textinput.getSelectionStart();
			int eSelection=et_pdf_textinput.getSelectionEnd();
			String selectedText = et_pdf_textinput.getText().toString().
					substring(sSelection, eSelection);
			int start = Math.max(et_pdf_textinput.getSelectionStart(), 0);
			int end = Math.max(et_pdf_textinput.getSelectionEnd(), 0);
			et_pdf_textinput.setText(et_pdf_textinput.getText().replace(Math.min(start, end), Math.max(start, end),
			        textToInsert, 0, textToInsert.length()));
		}else{//voice is disabled
			int start = Math.max(startSelection, 0);
			int end = Math.max(endSelection, 0);
			et_pdf_textinput.setText(et_pdf_textinput.getText().replace(Math.min(start, end), Math.max(start, end),
			        textToInsert, 0, textToInsert.length()));			
		}
		//Toast.makeText(this, "Text Deleted", Toast.LENGTH_SHORT).show();
	}
	///font
	private void Command_Font(){
		Command_Settings();
		//Toast.makeText(this, "Font", Toast.LENGTH_SHORT).show();
	}
	//Size
	private void Command_Size(){
		Command_Settings();
		//Toast.makeText(this, "Size", Toast.LENGTH_SHORT).show();
	}
	//Bold
	private void Command_Bold(){
		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==1){
			final SpannableStringBuilder str = new SpannableStringBuilder(
					et_pdf_textinput.getText().toString());
		    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 
		    		startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			et_pdf_textinput.setText(str);
				    
		}else{
			int sSelection=et_pdf_textinput.getSelectionStart();
			int eSelection=et_pdf_textinput.getSelectionEnd();
			final SpannableStringBuilder str = new SpannableStringBuilder(
					et_pdf_textinput.getText().toString());
		    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 
		    		sSelection, eSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		    et_pdf_textinput.setText(str);
		}
		//Toast.makeText(this, "Bold: ", Toast.LENGTH_SHORT).show();
	}
	//Italic
	private void Command_Italic(){
		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==1){
			final SpannableStringBuilder str = new SpannableStringBuilder(
					et_pdf_textinput.getText().toString());
		    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), 
		    		startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			et_pdf_textinput.setText(str);
				    
		}else{
			int sSelection=et_pdf_textinput.getSelectionStart();
			int eSelection=et_pdf_textinput.getSelectionEnd();
			final SpannableStringBuilder str = new SpannableStringBuilder(
					et_pdf_textinput.getText().toString());
		    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC), 
		    		sSelection, eSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		    et_pdf_textinput.setText(str);
		}
		//Toast.makeText(this, "Italics", Toast.LENGTH_SHORT).show();
	}
	//Underline
	private void Command_Underline(){
		if(settings.getInt(SettingsActivity.SET_TEXT_FORMATING, 1)==1){
			final SpannableStringBuilder str = new SpannableStringBuilder(
					et_pdf_textinput.getText().toString());
			String selectedText = et_pdf_textinput.getText().toString().
					substring(startSelection, endSelection);
			str.replace(startSelection, endSelection,"<u>"+selectedText_+"</u>");
		    et_pdf_textinput.setText(Html.fromHtml(str.toString()));
				    
		}else{
			int sSelection=et_pdf_textinput.getSelectionStart();
			int eSelection=et_pdf_textinput.getSelectionEnd();
			String selectedText = et_pdf_textinput.getText().toString().
					substring(sSelection, eSelection);
			final SpannableStringBuilder str = new SpannableStringBuilder(
					et_pdf_textinput.getText().toString());
			str.replace(sSelection, eSelection,"<u>"+selectedText+"</u>");
		    et_pdf_textinput.setText(Html.fromHtml(str.toString()));
		}
		//Toast.makeText(this, "Underline", Toast.LENGTH_SHORT).show();
	}
	
	///search dialog
	private void Search_Word(){
			settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
			final Dialog dialog_search = new Dialog(this);			
			dialog_search.setContentView(R.layout.dialog_search);
			dialog_search.setTitle("Search");
			final EditText et_find_keyword = (EditText) dialog_search.findViewById(R.id.et_dialog_search_find_kw);
			final EditText et_replace_keyword = (EditText) dialog_search.findViewById(R.id.et_dialog_search_replace_kw);
			ImageButton ib_mic = (ImageButton) dialog_search.findViewById(R.id.ib_dialog_search_mic);
	        Button bt_diag_search = (Button) dialog_search.findViewById(R.id.bt_dialog_search);
	        Button bt_diag_replace = (Button) dialog_search.findViewById(R.id.bt_dialog_replace);
	        
			settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);  
	        String col = settings.getString(SettingsActivity.SET_THEAM, "O");        
	        if(col.equals("O")){
	        	((TextView) dialog_search.findViewById(R.id.TextView01)).setTextColor(getResources().getColor(R.color.Orange));
	        	((TextView) dialog_search.findViewById(R.id.TextView02)).setTextColor(getResources().getColor(R.color.Orange));
	        	((TextView) dialog_search.findViewById(R.id.TextView001)).setTextColor(getResources().getColor(R.color.Orange));
	        	((TextView) dialog_search.findViewById(R.id.TextView002)).setTextColor(getResources().getColor(R.color.Orange));
	        	bt_diag_search.setBackgroundColor(getResources().getColor(R.color.Orange));
	        	bt_diag_replace.setBackgroundColor(getResources().getColor(R.color.Orange));
	        }else if(col.equals("B")){
	        	((TextView) dialog_search.findViewById(R.id.TextView01)).setTextColor(getResources().getColor(R.color.Blue));
	        	((TextView) dialog_search.findViewById(R.id.TextView02)).setTextColor(getResources().getColor(R.color.Blue));
	        	((TextView) dialog_search.findViewById(R.id.TextView001)).setTextColor(getResources().getColor(R.color.Blue));
	        	((TextView) dialog_search.findViewById(R.id.TextView002)).setTextColor(getResources().getColor(R.color.Blue));
	        	bt_diag_search.setBackgroundColor(getResources().getColor(R.color.Blue));
	        	bt_diag_replace.setBackgroundColor(getResources().getColor(R.color.Blue));	        
	        }else if(col.equals("G")){
	        	((TextView) dialog_search.findViewById(R.id.TextView01)).setTextColor(getResources().getColor(R.color.Black));
	        	((TextView) dialog_search.findViewById(R.id.TextView02)).setTextColor(getResources().getColor(R.color.Black));
	        	((TextView) dialog_search.findViewById(R.id.TextView001)).setTextColor(getResources().getColor(R.color.Black));
	        	((TextView) dialog_search.findViewById(R.id.TextView002)).setTextColor(getResources().getColor(R.color.Black));
	        	bt_diag_search.setBackgroundColor(getResources().getColor(R.color.Black));
	        	bt_diag_replace.setBackgroundColor(getResources().getColor(R.color.Black));	        
	        }
			ib_mic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					speech_to_text(REQ_CODE_SEARCH_DIALOG_INPUT);
					et_find_keyword.setText(settings.getString("dialog_find_keyWord", ""));
					et_replace_keyword.setText(settings.getString("dialog_replace_keyWord", ""));
				}
			});
			
	        bt_diag_search.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	//et_keyword = (EditText) findViewById(R.id.et_dialog_search_kw);
	            	Command_Search_Word(et_find_keyword.getText().toString());
	            	//Toast.makeText(getApplicationContext(), et_find_keyword.getText().toString(), 1).show();
	                dialog_search.dismiss();
	                
	            }
	        });
	        bt_diag_replace.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	//Toast.makeText(getApplicationContext(), et_find_keyword.getText().toString(), 1).show();
	            	et_pdf_textinput.setText(et_pdf_textinput.getText().toString().
	            			replaceAll(et_find_keyword.getText().toString(),
	            			et_replace_keyword.getText().toString()));
	            	dialog_search.dismiss();
	            }
	        });
	        dialog_search.show();	
        	Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
	}
	private void Command_Search_Word(String str){
    	StringReader sr= new StringReader(et_pdf_textinput.getText().toString()); // wrap your String
    	BufferedReader br= new BufferedReader(sr); // wrap your StringReader
		String dline=null;
		//StringBuilder final_str = new StringBuilder();
		backup_text = et_pdf_textinput.getText().toString();
		et_pdf_textinput.setText("");
		try {
			while((dline = br.readLine())!=null){
				String fullstr =  dline.replaceAll(str , "<font color='red'>"+str +"</font>");
				//str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
				//fullstr =  dline.replaceAll(str , "<font color='red'>"+str +"</font>");

				et_pdf_textinput.append(Html.fromHtml(fullstr));
				et_pdf_textinput.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//	String fullstr =  et_pdf_textinput.getText().toString().replaceAll(str , "<font color='red'>"+str +"</font>");
    //    et_pdf_textinput.setText(Html.fromHtml(fullstr), EditText.BufferType.SPANNABLE);
	}

	///////////////MENU COMMANDS/FUNCTIONS/METHODS/////////////////
	////////////////////////////////////////////////////////////////

	//OPEN TEXT DONE
	private void Open_File(String open_dir_path){
		//Toast.makeText(getApplicationContext(), "hihihih:::"+open_dir_path, 0).show();
		if(settings.getString("file_name_to_open","null_selected").equals("null_selected")){
			Toast.makeText(getApplicationContext(), "No File Selected", 0).show();
		}else{
		
		
		if(settings.getString("file_name_to_open", "aa").contains(".pdf")){
			  Intent intent = new Intent(Intent.ACTION_VIEW);
			  File file = new File(open_dir_path, settings.getString("file_name_to_open", "aa"));
			  intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
			     startActivity(intent);
			 
		}else if(settings.getString("file_name_to_open", "aa").contains(".txt")
				||settings.getString("file_name_to_open", "aa").contains(".cpp")
				||settings.getString("file_name_to_open", "aa").contains(".py")
				||settings.getString("file_name_to_open", "aa").contains(".java")
				||settings.getString("file_name_to_open", "aa").contains(".c")){
			
			BufferedReader bufferedReader=null;
			StringBuilder result = new StringBuilder();
	
				try {	
		  		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		  		File open_file = new File(open_dir_path,settings.getString("file_name_to_open", "aa"));
		  		  
				//InputStream fileInputStream = openFileInput(settings.getString("file_name_to_open", "aa"));
				bufferedReader = new BufferedReader(new FileReader(open_file));
				String data_line;
	
				while((data_line = bufferedReader.readLine())!=null){
						//result.append(data_line +"\r\n");
					result.append(data_line);
					result.append("\n");
					}
				 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
				et_pdf_textinput.setText(result);
		}
		}
	}
	
	//SAVE TEXT DONE
	private void Save_txt_file(String fn,String ft){
    	String data = et_pdf_textinput.getText().toString();
    	try {
            final File dir = new File(save_path + "/");
            if (!dir.exists())
                dir.mkdirs(); 
            final File myFile = new File(dir,
            		fn + ft);
            if (!myFile.exists()) 
                myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = 
                                    new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
            //Toast.makeText(getBaseContext(),"Done writing", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
           /* Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();*/
        	Toast.makeText(getBaseContext(), "Select Directory",
                    Toast.LENGTH_SHORT).show();
        	Command_Save();
        }	
	}
	
	private void Save_pdf_file(String fn,String ft){
	 	Document doc = new Document();
		 try {
			 	//String path = fn;
			 	File dir = new File(save_path+"/");
			        if(!dir.exists())
			        	dir.mkdirs();
 
			    File file = new File(dir, fn + ft);
			    if(!file.exists())
			    	file.createNewFile();
			    
			    FileOutputStream fOut = new FileOutputStream(file);
        	 	PdfWriter.getInstance(doc, fOut);
                //open the document
                doc.open();
                
                Paragraph p1 = new Paragraph(et_pdf_textinput.getText().toString());
                //int sz = settings.getInt(SettingsActivity.SIZE_OF_TEXT, 25);
                Font paraFont= new Font(Font.HELVETICA,14f);
                String ali = settings.getString(SettingsActivity.SET_ALIGNMENT, "L");
        		if(ali.equals("L")){
        			p1.setAlignment(Paragraph.ALIGN_LEFT);
        		}else if(ali.equals("C")){
        			p1.setAlignment(Paragraph.ALIGN_CENTER);
        		}else if(ali.equals("R")){
        			p1.setAlignment(Paragraph.ALIGN_RIGHT);
        		}
                p1.setFont(paraFont);
                 //add paragraph to document    
                 doc.add(p1);
                 /*
                 Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
                 Font paraFont2= new Font(Font.COURIER,50.0f,Color.GREEN);
                 p2.setAlignment(Paragraph.ALIGN_CENTER);
                 p2.setFont(paraFont2);
                 doc.add(p2);*/
                 ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.ic_launcher);
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
                 Image myImg = Image.getInstance(stream.toByteArray());
                 myImg.setAlignment(Image.MIDDLE);
                 //add image to document
                 //doc.add(myImg);
                 //set footer
                 Phrase footerText = new Phrase("VoicePad");
                 HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
                 doc.setFooter(pdfFooter);
      
         } catch (DocumentException de) {
                 Log.e("PDFCreator", "DocumentException:" + de);
         } catch (IOException e) {
                 Log.e("PDFCreator", "ioException:" + e);
         }finally{
                doc.close();
         }
		Toast.makeText(getBaseContext(), "PDF",
                Toast.LENGTH_SHORT).show();
	}
	
	private void Save_docx_file(String fn,String ft){
		
	}
	
	private void Command_Share(){
		
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey!, download this app from arkTech "+applink);
        startActivity(shareIntent);
	}
	
	
	
	
	@Override
	protected void onRestart() {
		setAttri();
		FULLScreen();
		super.onRestart();
	}
	@Override
	protected void onResume() {
		setAttri();
		FULLScreen();
		super.onResume();
	}
	@Override
	protected void onPause() {
		//animate transition
		overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
		super.onPause();
	}
}
