package com.example.hangman.mainactivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.example.hangman.settings.Settings;

import android.content.Context;
import android.util.Log;

public class GameState
{
	public String word, keyLog;
	public Settings settings;
	
	public GameState()
	{
		reset(new Settings());
	}
	
	public void reset(Settings settings)
	{
		word = keyLog = "";
		this.settings = settings;
	}
	
	public void updateState(char letter)
	{
		if(keyLog.indexOf(letter) == -1) keyLog += letter;
	}
	
	public void load(Context context)
	{
		try
    	{
    		InputStream stream = context.openFileInput("gamestate.txt");
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		String line = bufferedReader.readLine();    		
    		if(line != null)
    		{
    			String[] tokens = line.split(";");
    			if(tokens.length == 3 || tokens.length == 4)
    			{
    				settings.isEvil = tokens.length == 3;
    				settings.maxTries = Integer.parseInt(tokens[0]);
    				settings.wordLength = Integer.parseInt(tokens[1]);
    				keyLog = tokens[2];
    				if(tokens.length == 4) word = tokens[3];
    			}
    		}
    		
    		bufferedReader.close();
    		inputStreamReader.close();
    		stream.close();
    	}
    	catch(IOException e)
    	{
    	}
	}
	
	public void save(Context context)
	{
		try
		{			
			OutputStream outStream = context.openFileOutput("gamestate.txt", Context.MODE_PRIVATE);
			OutputStreamWriter stream = new OutputStreamWriter(outStream);
			BufferedWriter writer = new BufferedWriter(stream); 
			
			writer.write(Integer.toString(settings.maxTries) + ";");
			writer.write(Integer.toString(settings.wordLength) + ";");
			writer.write(keyLog + ";");
			if(!settings.isEvil) writer.write(word);
			
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
