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
		Vector<Vector<Integer>> classes = getEquivalenceClasses(letter);
		Vector<Vector<String>> wordGroups = getWordGroups(classes, letter);
		findLargestClass(wordGroups, classes, letter);
	}
	
	private void filterWords()
	{
		Vector<String> filteredWords = new Vector<String>();
		
		for (String word : this.words)		
			if (word.length() == this.length)
				filteredWords.add(word);	
		this.words = filteredWords;
	}
	
	private Vector<Vector<Integer>> getEquivalenceClasses(char letter)
	{
		Vector<Vector<Integer>> classes = new Vector<Vector<Integer>>();
		
		for (String word : words)
		{
			if (word.contains("" + letter))
			{				
				Vector<Integer> indices = new Vector<Integer>();
				for (int i = -1; (i = word.indexOf(letter, i + 1)) != -1;)					
					indices.add(i);				
				
				boolean isUnique = true;				
				for (Vector<Integer> definition : classes)
				{
					if (definition.size() != indices.size())					
						continue;
					
					int counter = 0;
					for (int i = 0; i < definition.size(); i++)
					{
						if (definition.get(i) == indices.get(i))						
							counter++;												 
					}		
					if (counter == definition.size()) isUnique = false;
				}				
				if (isUnique) classes.add(indices);				
			}			
		}
		return classes;
	}
	
	private Vector<Vector<String>> getWordGroups(Vector<Vector<Integer>> equivalenceClasses, char letter)
	{		
		Vector<Vector<String>> wordGroups = new Vector<Vector<String>>();			
				
		for (Vector<Integer> indices : equivalenceClasses)
		{			
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
					matchedWords.add(word);
			}
			for (String word : matchedWords)
				words.remove(word);
			
			wordGroups.add(matchedWords);			
		}		
		wordGroups.add(this.words);
		return wordGroups;
	}
	
	private void findLargestClass(Vector<Vector<String>> wordGroups, Vector<Vector<Integer>> equivalenceClasses, char letter)
	{
		int maxValue = 0, id = 0;
		
		for (Vector<String> group : wordGroups)
		{
			int sum = 0;
			for (String word : group)			
				sum += getUniqueChars(word);
			
			if (sum > maxValue)
			{
				maxValue = sum;
				id = wordGroups.indexOf(group);
			}				
		}
		
		boolean wrongGuess = (id == wordGroups.size() - 1);		
		
		this.words = wordGroups.get(id);
		listener.onGuess(!wrongGuess, letter);		
	
		if (wrongGuess)
		{
			Log.d("Handman", "" + this.tries);
			this.tries++;
			return;
		}
		
		StringBuilder updatedGuess = new StringBuilder(guess);
		for (int index : equivalenceClasses.get(id))			
			updatedGuess.setCharAt(index, letter);
		
		this.guess = updatedGuess.toString();
		
		Log.d("Handman", "" + this.tries);
		
		if (finished()) this.listener.onWin(this.words.get(0));
		else if (lost()) this.listener.onLose(this.words.get(0));
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
