package com.example.hangman;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import android.util.Log;

public class GoodGameplay extends Gameplay
{
	private String word;
	
	public GoodGameplay(Vector<String> words, int length, int tries, GameplayListener listener)
	{
		super(words, length, tries, listener);
		
		chooseWord();
		Log.d("Hangman", word);
	}
	
	@Override
	public void guess(char letter)	
	{	
		this.tries++;
		if (word.contains("" + letter))
		{
			updateGuess(letter);			
			listener.onGuess(true, letter);
		}
		else
			listener.onGuess(false, letter);
	}
	
	private void updateGuess(char letter)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = -1; (i = word.indexOf(letter, i + 1)) != -1;)		
			indices.add(i);
		
		StringBuilder updatedGuess = new StringBuilder(guess);
		for (int index : indices)
			updatedGuess.setCharAt(index, letter);
		
		this.guess = updatedGuess.toString();
				
		if (finished()) listener.onWin(word);
		if (lost()) listener.onLose(word);	
	}
	
	private void chooseWord()
	{
		ArrayList<String> words = new ArrayList<String>();
		
		for (String word : this.words)		
			if (word.length() == this.length)
				words.add(word);		
		
		Random generator = new Random();
		int index = generator.nextInt(words.size());
		this.word = words.get(index).toUpperCase(Locale.UK);
	}
}
