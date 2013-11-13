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
	}
		
	@Override
	public void guess(char letter)	
	{		
		boolean succes = this.word.contains("" + letter);
		
		if (succes) updateGuess(letter);		
		else this.tries++;
			
		this.listener.onGuess(succes, letter);					
	}
	
	private void updateGuess(char letter)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = -1; (i = this.word.indexOf(letter, i + 1)) != -1;)		
			indices.add(i);
		
		StringBuilder updatedGuess = new StringBuilder(this.guess);
		for (int index : indices)
			updatedGuess.setCharAt(index, letter);
		
		this.guess = updatedGuess.toString();
				
		if (finished()) this.listener.onWin(this.word);
		else if (lost()) this.listener.onLose(this.word);	
	}
	
	private void chooseWord()
	{
		Random generator = new Random();
		do
		{
			int index = generator.nextInt(this.words.size());
			this.word = this.words.get(index).toUpperCase(Locale.UK);			
		} while (this.word.length() != this.length);		
	}
}
