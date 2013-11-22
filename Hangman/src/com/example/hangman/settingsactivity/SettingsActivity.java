package com.example.hangman.settingsactivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.hangman.R;

public class SettingsActivity extends Activity {
	private boolean isEvil;
	private int maxTries;
	private int wordLength;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.settings);
	}
	
	private void load()
	{	
		try
    	{
    		InputStream stream = openFileInput("settings.txt");
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		isEvil = Boolean.parseBoolean(bufferedReader.readLine());
    		maxTries = Integer.parseInt(bufferedReader.readLine());
    		wordLength = Integer.parseInt(bufferedReader.readLine());
    	}
    	catch(IOException e)
    	{
    		Log.e("loadScores", e.getMessage());
    	}
	}
	
	private void save()
	{
		try
		{
			OutputStream outStream = openFileOutput("scores.txt", MODE_PRIVATE);
			OutputStreamWriter stream = new OutputStreamWriter(outStream);
			BufferedWriter writer = new BufferedWriter(stream); 
			writer.write("" + isEvil);
			writer.newLine();
			writer.write(maxTries);
			writer.newLine();
			writer.write(wordLength);
			writer.newLine();
			writer.close();
		}
		catch(IOException e)
    	{
    		Log.e("saveScore", e.getMessage());
    	}
	}

}
