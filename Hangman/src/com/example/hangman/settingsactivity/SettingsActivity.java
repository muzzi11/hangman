package com.example.hangman.settingsactivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.example.hangman.R;
import com.example.hangman.settings.Settings;

public class SettingsActivity extends Activity 
{
	Settings settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settings);
	    
	    settings = new Settings();   
	    settings.load(this);
	    initialize();
	}		
			
	private void initialize()
	{
		final TextView tries = (TextView) findViewById(R.id.textMaxTries);
	    final TextView length = (TextView) findViewById(R.id.textWordLength);
	    
	    tries.setText("" + settings.maxTries);
	    length.setText("" + settings.wordLength);
	    
	    final SeekBar seekBarTries = (SeekBar) findViewById(R.id.seekBarTries);
	    final SeekBar seekBarLength = (SeekBar) findViewById(R.id.seekBarWordLength);
	    	    	  
	    seekBarTries.setProgress(settings.maxTries);
	    seekBarLength.setProgress(settings.wordLength);
	    
	    final Context context = this;
	    
	    seekBarTries.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
	    {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) 
			{			
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{	
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{
				tries.setText("" + progress);
				settings.maxTries = progress;
				settings.save(context);
			}
		});
	    
	    seekBarLength.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
	    {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) 
			{				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{
				length.setText("" + progress);
				settings.wordLength = progress;
				settings.save(context);
			}
	    });	
	    
	    CheckBox mode = (CheckBox) findViewById(R.id.checkBoxMode);
	    mode.setChecked(settings.isEvil);
	    mode.setOnClickListener(new OnClickListener() 
	    {			
			@Override
			public void onClick(View v) 
			{
				settings.isEvil = ((CheckBox) v).isChecked();
				settings.save(context);
			}
		});
	}
}
