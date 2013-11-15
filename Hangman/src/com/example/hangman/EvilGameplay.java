package com.example.hangman;

import java.util.ArrayList;
import java.util.HashSet;
import android.util.Log;

public class EvilGameplay extends Gameplay
{		
	public EvilGameplay(ArrayList<String> words, int length, int tries, GameplayListener listener)
	{
		super(words, length, tries, listener);		
		
		filterWords();
	}
	
	@Override
	public void guess(char letter)	
	{					
		ArrayList<ArrayList<Integer>> classes = getEquivalenceClasses(letter);
		ArrayList<ArrayList<String>> wordGroups = getWordGroups(classes, letter);
		findLargestClass(wordGroups, classes, letter);
	}
	
	private void filterWords()
	{
		ArrayList<String> filteredWords = new ArrayList<String>();
		
		for (String word : this.words)		
			if (word.length() == this.length)
				filteredWords.add(word);	
		this.words = filteredWords;
	}
	
	private ArrayList<ArrayList<Integer>> getEquivalenceClasses(char letter)
	{
		ArrayList<ArrayList<Integer>> classes = new ArrayList<ArrayList<Integer>>();
		
		for (String word : words)
		{
			if (word.contains("" + letter))
			{				
				ArrayList<Integer> indices = new ArrayList<Integer>();
				for (int i = -1; (i = word.indexOf(letter, i + 1)) != -1;)					
					indices.add(i);				
				
				boolean isUnique = true;				
				for (ArrayList<Integer> definition : classes)
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
	
	private ArrayList<ArrayList<String>> getWordGroups(ArrayList<ArrayList<Integer>> equivalenceClasses, char letter)
	{		
		ArrayList<ArrayList<String>> wordGroups = new ArrayList<ArrayList<String>>();			
				
		for (ArrayList<Integer> indices : equivalenceClasses)
		{			
			ArrayList<String> matchedWords = new ArrayList<String>();			
			
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
	
	private void findLargestClass(ArrayList<ArrayList<String>> wordGroups, ArrayList<ArrayList<Integer>> classes, char letter)
	{
		int maxValue = 0, id = 0;
		
		for (ArrayList<String> group : wordGroups)
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
		for (int index : classes.get(id))			
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
