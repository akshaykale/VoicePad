package com.theark.notepadsq;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * DIR
Copyright (C) 2011 by Brad Greco <brad@bgreco.net>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

@SuppressLint("NewApi")
public class DirectoryPicker extends ListActivity {

	int count=0;
	public static final String START_DIR = "startDir";
	public static final String ONLY_DIRS = "onlyDirs";
	public static final String SHOW_HIDDEN = "showHidden";
	public static final String CHOSEN_DIRECTORY = "chosenDir";
	public static final int PICK_DIRECTORY = 43522432;
	private File dir;
	private boolean showHidden = false;
	private boolean onlyDirs = true ;
	private static final String PREFS_NAME = "funct";

	SharedPreferences funSP;
	private String func;
	String name;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        Bundle extras = getIntent().getExtras();
        dir = Environment.getExternalStorageDirectory();
        if (extras != null) {
        	String preferredStartDir = extras.getString(START_DIR);
        	showHidden = extras.getBoolean(SHOW_HIDDEN, false);
        	func = extras.getString("function_to_do");
        	onlyDirs = extras.getBoolean(ONLY_DIRS, true);
        	//Toast.makeText(this, func, 0).show();

        	if(preferredStartDir != null) {
            	File startDir = new File(preferredStartDir);
            	if(startDir.isDirectory()) {
            		dir = startDir;
            	}
            } 
        }
        
        setContentView(R.layout.chooser_list);
        setTitle(dir.getAbsolutePath());

        final Button btnChoose = (Button) findViewById(R.id.btnChoose);
        
        ActionBar bar = getActionBar();

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);  
        String col = settings.getString(SettingsActivity.SET_THEAM, "O");        
        if(col.equals("O")){
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Orange)));
        	btnChoose.setBackgroundColor(getResources().getColor(R.color.Orange));
        }else if(col.equals("B")){
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Blue)));
        	btnChoose.setBackgroundColor(getResources().getColor(R.color.Blue));	        
        }else if(col.equals("G")){
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Black)));
        	btnChoose.setBackgroundColor(getResources().getColor(R.color.Black));
        	btnChoose.setTextColor(getResources().getColor(R.color.Black));
        }
        funSP = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = funSP.edit();
        editor.putString("file_name_to_open", "null_selected");
	    editor.commit();
        
        name = dir.getName();
        if(name.length() == 0)
        	name = "/";
        btnChoose.setText("Choose " + "'" + name + "'");
        btnChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	/*if(!func.equals("save")){
            		if(count!=0)
            			returnDir(dir.getAbsolutePath());
            	}else*/
            		returnDir(dir.getAbsolutePath());
            }
        });
        
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        if(!dir.canRead()) {
        	Context context = getApplicationContext();
        	String msg = "Could not read folder contents.";
        	Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        	toast.show();
        	return;
        }
        
        final ArrayList<File> files = filter(dir.listFiles(), onlyDirs, showHidden);
        String[] names = names(files);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, names));        	
        

        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		
        		if(!files.get(position).isDirectory()){
        			//count++;
        			String fname = files.get(position).getName();

        			funSP = getSharedPreferences(PREFS_NAME, MODE_WORLD_WRITEABLE);
        	        SharedPreferences.Editor editor = funSP.edit();
        	        editor.putString("file_name_to_open", fname);
        		    editor.commit();
        			//Toast.makeText(getApplicationContext(), ":::"+fname, 0).show();
        			return;
        		}
        		
        		String path = files.get(position).getAbsolutePath();
                Intent intent = new Intent(DirectoryPicker.this, DirectoryPicker.class);
                intent.putExtra(DirectoryPicker.START_DIR, path);
                intent.putExtra(DirectoryPicker.SHOW_HIDDEN, showHidden);
                intent.putExtra(DirectoryPicker.ONLY_DIRS, onlyDirs);
                startActivityForResult(intent, PICK_DIRECTORY);
        	}
        });
    }
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == PICK_DIRECTORY && resultCode == RESULT_OK) {
	    	Bundle extras = data.getExtras();
	    	String path = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
	        returnDir(path);
    	}
    }
	
    private void returnDir(String path) {
    	Intent result = new Intent();
    	result.putExtra(CHOSEN_DIRECTORY, path);
        setResult(RESULT_OK, result);
    	finish();    	
    }

	public ArrayList<File> filter(File[] file_list, boolean onlyDirs, boolean showHidden) {
		ArrayList<File> files = new ArrayList<File>();
		for(File file: file_list) {
			if(onlyDirs && !file.isDirectory())
				continue;
			if(!showHidden && file.isHidden())
				continue;
			files.add(file);
		}
		Collections.sort(files);
		return files;
	}
	
	public String[] names(ArrayList<File> files) {
		String[] names = new String[files.size()];
		int i = 0;
		for(File file: files) {
			names[i] = file.getName();
			i++;
		}
		return names;
	}
	
	@Override
	protected void onPause() {
		//animate transition
		overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
		super.onPause();
	}
}

