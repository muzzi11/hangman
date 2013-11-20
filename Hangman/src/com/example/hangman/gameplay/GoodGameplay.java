package com.example.hangman.gameplay;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GoodGameplay extends Gameplay
{
	private String word;
	private ArrayList<String> words;
	
	public GoodGameplay(ArrayList<String> words, int length, int tries, GameplayListener listener)
	{
		super(length, tries, listener);		
		
		ArrayList<String> filteredWords = new ArrayList<String>();
		for (String word : words)		
			if (word.length() == length) filteredWords.add(word);
		this.words = filteredWords;
		
		chooseRandomWord();
	}
	
	private void chooseRandomWord()
	{
		Random generator = new Random();
		int index = generator.nextInt(words.size());
		word = words.get(index).toUpperCase(Locale.UK);	
	}
		
	@Override
	public boolean guess(char letter)	
	{		
		boolean letterGuessed = word.contains("" + letter);
		
		if (letterGuessed)
		{
			StringBuilder updatedGuess = new StringBuilder(guess);
			for(int i = 0; i < word.length(); ++i)
			{
				if(word.charAt(i) == letter) updatedGuess.setCharAt(i, letter);
			}
			guess = updatedGuess.toString();
		}
		else
		{
			tries++;
		}
		
		if (won()) listener.onWin(word);
		else if (lost()) listener.onLose(word);
		
		return letterGuessed;
	}
}
