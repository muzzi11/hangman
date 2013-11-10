package com.example.hangman;

import java.util.ArrayList;
import java.util.Random;

public class GoodGameplay extends Gameplay
{
	private String word;
	
	public GoodGameplay(String[] words, int length, int tries, GameplayListener listener)
	{
		super(words, length, tries, listener);
		
		chooseWord();
	}
	
	@Override
	public Boolean guess(char letter)
	{
		if (word.contains("" + letter))
		{
			updateGuess(letter);
			return true;
		}
		return false;
	}
	
	private void updateGuess(char letter)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = -1; (i = word.indexOf(letter, i + 1)) != -1;)		
			indices.add(i);
		
		StringBuilder updatedGuess = new StringBuilder(guess);
		for (int index : indices)
			updatedGuess.setCharAt(index, letter);
		
		guess = updatedGuess.toString();
	}
	
	private void chooseWord()
	{
		ArrayList<String> words = new ArrayList<String>();
		
		for (String word : this.words)		
			if (word.length() == this.length)
				words.add(word);		
		
		Random generator = new Random();
		int index = generator.nextInt(words.size());
		this.word = words.get(index);
	}
}
