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
import java.util.Comparator;
import java.util.HashSet;

import android.content.Context;
import android.util.Log;

public class History 
{
	private final int maxHighscores = 20;
	private  ArrayList<HistoryEntry> scores = new ArrayList<HistoryEntry>();
	
	private OutputStream outStream;
	
	public History(Context context)
	{
		load(context);
		
		try 
		{
			outStream = context.openFileOutput("scores.txt", Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) 
		{
			Log.e("loadOutputstream", e.getMessage());
		}
	}
	
	public ArrayList<HistoryEntry> getEntries()
	{
		return scores;
	}
	
	public void score(String word, int tries)
	{
		int score = (int)(((float)getUniqueChars(word) / (float)tries) * 1000.0f);
		Log.d("Hangman", "Score: " + score);
		
		int i;
		for(i = 0; i < scores.size(); ++i)
		{
			if(score > scores.get(i).score) break;
		}
		
		if(i < maxHighscores)
		{
			HistoryEntry entry = new HistoryEntry();
			entry.word = word;
			entry.score = score;
			scores.add(i, entry);
			
			save();
		}
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
	
	private void load(Context context)
	{
		scores.clear();
		
		try
    	{
    		InputStream stream = context.openFileInput("scores.txt");
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		String line;    		
    		while((line = bufferedReader.readLine()) != null && scores.size() < maxHighscores)
    		{
    			HistoryEntry entry = new HistoryEntry();
    			entry.fromString(line);
    			scores.add(entry);
    		}
    		
    		bufferedReader.close();
    		inputStreamReader.close();
    		stream.close();

    		Collections.sort(scores, new Comparator<HistoryEntry>()
    		{
    			@Override
    			public int compare(HistoryEntry lhs, HistoryEntry rhs)
    			{
    				return rhs.score - lhs.score;
    			}
    		});
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
			OutputStreamWriter stream = new OutputStreamWriter(outStream);
			BufferedWriter writer = new BufferedWriter(stream);
			
			for(HistoryEntry entry : scores)
			{
				writer.write(entry.toString());
				writer.newLine();
			}
			
			writer.close();
			stream.close();
			outStream.close();
		}
		catch(IOException e)
    	{
    		Log.e("saveScore", e.getMessage());
    	}
	}
}
