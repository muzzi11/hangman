package com.example.hangman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class EvilGameplay extends Gameplay
{		
	private Map<String, Integer> uniqueChars;
	
	public EvilGameplay(ArrayList<String> words, int length, int tries, GameplayListener listener)
	{
		super(words, length, tries, listener);		
		
		ArrayList<String> filteredWords = new ArrayList<String>();
		for (String word : words)		
			if (word.length() == length) filteredWords.add(word);
		this.words = filteredWords;
	}
	
	@Override
	public void guess(char letter)	
	{			
		long start, end;		
		start = System.currentTimeMillis();
		
		ArrayList<String> containLetter = new ArrayList<String>();
		ArrayList<String> noContainLetter = new ArrayList<String>();
		for(String word : words)
		{
			if(word.indexOf(letter) != -1) containLetter.add(word);
			else noContainLetter.add(word);
		}
		
		if(containLetter.size() > noContainLetter.size())
		{
			int c = getBestClass(containLetter, letter);
			
			// Filter all words that are not of the same class
			ArrayList<String> newWords = new ArrayList<String>();
			for(String word : containLetter)
			{
				if(classifyWord(word, letter) == c) newWords.add(word);
			}
			words = newWords;
			
			// Update the guess
			StringBuilder updatedGuess = new StringBuilder(guess);
			for (int i = 0; c != 0; c = c >> 1, ++i)
			{
				if((c & 1) > 0) updatedGuess.setCharAt(i, letter);
			}
			guess = updatedGuess.toString();
			
			listener.onGuess(true, letter);
		}
		else
		{
			words = noContainLetter;
			++tries;
			listener.onGuess(false, letter);
		}
		if(finished()) listener.onWin(guess);
		if(lost()) listener.onLose(guess);
		
		end = System.currentTimeMillis();
		Log.d("Hangman", "" + (end - start));
	}
	
	private int classifyWord(String word, char letter)
	{
		int c = 0;
		
		for(int i = 0; i < word.length(); ++i)
		{
			c = c << 1;
			if(word.charAt(i) == letter) c |= 1;
		}
		
		return c;
	}
	
	private int getBestClass(ArrayList<String> samples, char letter)
	{
		Map<Integer, Integer> classes = new HashMap<Integer, Integer>();
		
		// Count frequencies of classes
		for(String sample : samples)
		{
			int c = classifyWord(sample, letter);
			Integer freq = classes.get(c);
			classes.put(c, freq == null ? 1 : freq + 1);
		}
		
		// Find highest frequency
		Map.Entry<Integer, Integer> maxEntry = null;
		for(Map.Entry<Integer, Integer> entry : classes.entrySet())
		{
			if(maxEntry == null || entry.getValue() > maxEntry.getValue()) maxEntry = entry;
		}
		
		return maxEntry.getKey();
	}
}