package com.example.hangman.gameplay;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.util.Log;

public class GoodGameplay extends Gameplay
{
	public String word;	
	
	public GoodGameplay(ArrayList<String> words, int length, int tries, GameplayListener listener)
	{
		super(length, tries, listener);	
		Log.d("Hangman", "Length: " + length);		
		chooseRandomWord(words);		
		Log.d("Hangman", "Word: " + word);
	}
	
	private void chooseRandomWord(ArrayList<String> words)
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
		
		if (won()) listener.onWin(word, tries);
		else if (lost()) listener.onLose(word);
		
		return letterGuessed;
	}
}
