package com.example.hangman.history;

public class HistoryEntry
{
	public String word = "";
	public int score = 0;
	
	@Override
	public String toString()
	{
		return word + ";" + Integer.toString(score);
	}
	
	public void fromString(String str)
	{
		String[] tokens = str.split(":");
		if(tokens.length == 2)
		{
			word = tokens[0];
			score = Integer.parseInt(tokens[1]);
		}
	}
}