package com.example.hangman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import android.util.Log;
import android.util.SparseIntArray;

public class EvilGameplay extends Gameplay
{		
	public EvilGameplay(Vector<String> words, int length, int tries, GameplayListener listener)
	{
		super(words, length, tries, listener);		
		
		filterWords();
	}
	
	@Override
	public void guess(char letter)	
	{	
		this.tries++;
		getWordGroup(letter);		
	}
	
	private void filterWords()
	{
		Vector<String> filteredWords = new Vector<String>();
		
		for (String word : this.words)		
			if (word.length() == this.length)
				filteredWords.add(word);	
		words = filteredWords;
	}
	
	private void getWordGroup(char letter)
	{
		Vector<Vector<String>> classes = new Vector<Vector<String>>();
		Vector<Vector<Integer>> classDefinitions = new Vector<Vector<Integer>>();
		SparseIntArray uniqueCharCount = new SparseIntArray();
		
		Vector<String> emptyClass = new Vector<String>();
		int emptyClassSize = 0;
		
		Log.d("Hangman", "Total words: " + words.size());
		if (words.size() == 1 ) Log.d("Hangman", "last word: " + words.get(0)); 	
		
		// Get class specifications.
		for (String word : words)
		{
			if (word.contains("" + letter))
			{				
				Vector<Integer> indices = new Vector<Integer>();
				for (int i = -1; (i = word.indexOf(letter, i + 1)) != -1;)					
					indices.add(i);				
				
				boolean isUnique = true;				
				for (Vector<Integer> definition : classDefinitions)
				{
					if (definition.size() != indices.size())					
						continue;
					
					int counter = 0;
					for (int i = 0; i < definition.size(); i++)
					{
						if (definition.get(i) == indices.get(i))
						{
							counter++;
						}						 
					}		
					if (counter == definition.size()) isUnique = false;
				}				
				if (isUnique) classDefinitions.add(indices);				
			}	
			else
			{
				emptyClass.add(word);
				emptyClassSize += getUniqueChars(word);
			}
		}
		
		Log.d("Hangman", "Classes: " + classDefinitions.size());
		
		// Find matching words and count unique characters
		int classID = 0;
		for (Vector<Integer> indices : classDefinitions)
		{
			Vector<String> remainderWords = new Vector<String>();
			Vector<String> matchedWords = new Vector<String>();
						
			for (String word : words)
			{
				boolean isEqual = true;
				
				int occurences = 0;
				for (int i = -1; (i = word.indexOf(letter, i + 1)) != -1;)
					occurences++;
				
				if (indices.size() == occurences)
				{
					for (int index : indices)
						if (word.charAt(index) != letter) isEqual = false;
				}
				else
					isEqual = false;				
				
				if (isEqual) 
				{
					matchedWords.add(word);
					if (uniqueCharCount.indexOfKey(classID) > -1)
						uniqueCharCount.put(classID, uniqueCharCount.get(classID) + getUniqueChars(word));
					else
						uniqueCharCount.put(classID, getUniqueChars(word));
				}
				else remainderWords.add(word);
			}
			words = remainderWords;
			classes.add(matchedWords);
			classID++;
		}
		
		// Find largest class
		int maxValue = 0, id = 0;
		for (int i = 0; i < uniqueCharCount.size(); i++)
		{
			int count = uniqueCharCount.get(i);
			if (count > maxValue)
			{
				maxValue = count;
				id = i;
			}		
		}
		Log.d("Hangman", "empty class: " + emptyClassSize);
		Log.d("Hangman", "other class: " + maxValue);
		
			
		if (emptyClassSize > maxValue) 
		{
			this.words = emptyClass;
			listener.onGuess(false, letter);
		}
		else
		{
			words = classes.get(id);
		
			StringBuilder updatedGuess = new StringBuilder(guess);
			for (int index : classDefinitions.get(id))			
				updatedGuess.setCharAt(index, letter);
			
			this.guess = updatedGuess.toString();
			listener.onGuess(true, letter);
		}
				
		//if (finished()) listener.onWin(word);
		//else if (lost()) listener.onLose(word);	
	}
	
	private int getUniqueChars(String word)
	{
		HashSet<Character> set = new HashSet<Character>();    

		for (int i = 0; i < word.length(); i++) {
		    char c = word.charAt(i);
		    set.add(c);
		}
		return set.size();
	}
}
