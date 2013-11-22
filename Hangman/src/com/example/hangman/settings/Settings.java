package com.example.hangman.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.util.Log;

public class Settings 
{	
	public boolean isEvil = false;
	public int maxTries = 0;
	public int wordLength = 0;
	
	private Activity activity;
	
	public Settings(Activity activity)
	{		
		this.activity = activity;
		load();
	}
	
	private void load()
	{	
		try
    	{
    		InputStream stream = activity.openFileInput("settings.txt");
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		String line = bufferedReader.readLine();    		
    		isEvil = Boolean.parseBoolean(line);
    		    		
    		line = bufferedReader.readLine();    		
    		if (line != null) maxTries = Integer.parseInt(line);
    		
    		line = bufferedReader.readLine();    		
    		if (line != null) wordLength = Integer.parseInt(line);
    		
    		bufferedReader.close();
    	}
    	catch(IOException e)
    	{
    		Log.e("loadScores", e.getMessage());
    	}		
	}
	
	public void save()
	{
		try
		{			
			OutputStream outStream = activity.openFileOutput("settings.txt", activity.MODE_PRIVATE);
			OutputStreamWriter stream = new OutputStreamWriter(outStream);
			BufferedWriter writer = new BufferedWriter(stream); 
						
			writer.write("" + isEvil);
			writer.newLine();
						
			writer.write("" + maxTries);
			writer.newLine();
						
			writer.write("" + wordLength);
			writer.newLine();
			
			writer.close();
			stream.close();
			outStream.close();
		}
		catch(IOException e)
    	{
    		Log.e("Hangman", e.getMessage());
    	}
	}
}
