package com.example.hangman.settingsactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.RadioButton;

import com.example.hangman.R;
import com.example.hangman.mainactivity.MainActivity;
import com.example.hangman.settings.Settings;

public class SettingsActivity extends Activity 
{
	Settings settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settings);
	    
	    settings = new Settings(this);   
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
				settings.save();
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
				settings.save();
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
				settings.save();
			}
		});
	}
}
