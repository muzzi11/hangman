package com.example.hangman.history;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import android.content.Context;
import android.util.Log;

public class History 
{
	public ArrayList<Integer> scores;
	
	private OutputStream outStream;
	
	public History(Context context)
	{
		load(context);
		
		try 
		{
			outStream = context.openFileOutput("scores.txt", Context.MODE_APPEND);
		} catch (FileNotFoundException e) 
		{
			Log.e("loadOutputstream", e.getMessage());
		}
	}
	
	private void load(Context context)
	{
		scores = new ArrayList<Integer>();
		try
    	{
    		InputStream stream = context.openFileInput("scores.txt");
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		String line;    		
    		while((line = bufferedReader.readLine()) != null)
    		{
    			scores.add(Integer.parseInt(line));
    		}
    		
    		bufferedReader.close();
    		inputStreamReader.close();
    		stream.close();
    		
    		Collections.sort(scores);
    		Collections.reverse(scores);    		
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
			BufferedWriter writer = new BufferedWriter(stream); 
			writer.write("" + score);
			writer.newLine();
			writer.close();
			stream.close();
			outStream.close();
		}
		catch(IOException e)
    	{
    		Log.e("saveScore", e.getMessage());
    	}
	}
	
	public void score(String word, int tries)
	{
		int score = (int)(((float)getUniqueChars(word) / (float)tries) * 1000.0f);
		Log.d("Hangman", "Score: " + score);
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
