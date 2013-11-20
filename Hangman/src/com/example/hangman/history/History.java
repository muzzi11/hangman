package com.example.hangman.history;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.util.Log;

public class History 
{
	public ArrayList<String> scores;
	
	private OutputStream outStream;
	
	public History(Activity activity)
	{
		load(activity);
		
		try {
			outStream = activity.openFileOutput("scores.txt", activity.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			Log.e("loadOutputstream", e.getMessage());
		}
	}
	
	private void load(Activity activity)
	{
		scores = new ArrayList<String>();
		try
    	{
    		InputStream stream = activity.getAssets().open("scores.txt");
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		String line;
    		while((line = bufferedReader.readLine()) != null)
    		{
    			scores.add(line);
    		}
    		
    		bufferedReader.close();
    	}
    	catch(IOException e)
    	{
    		Log.e("loadScores", e.getMessage());
    	}
	}
	
	private void save(int score)
	{
		try
		{
			OutputStreamWriter stream = new OutputStreamWriter(outStream);
			stream.write("" + score);
			stream.close();
		}
		catch(IOException e)
    	{
    		Log.e("saveScore", e.getMessage());
    	}
	}
	
	public void Score(String word, int tries)
	{
		int score = (getUniqueChars(word) / tries) * 1000;
		save(score);
	}
	
	private int getUniqueChars(String word)
    {
        HashSet<Character> set = new HashSet<Character>();    

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c != '-') set.add(c);
        }
        return set.size();
    }
}
