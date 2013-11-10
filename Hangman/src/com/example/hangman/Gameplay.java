package com.example.hangman;

public class Gameplay 
{
	public String guess;
	public GameplayListener listener;
	public int tries;	
	public int length;
	
	public String[] words;
	
	Gameplay(String[] words, int length, int tries, GameplayListener listener)
	{
		this.words = words;
		this.length = length;
		this.tries = tries;
		this.listener = listener;
		
		guess = "";
		for (int i = 0; i < length; i++)
			guess += "_";
	}
	
	public String getGuess()
	{
		return guess;
	}
	
	public int getMaxTries()
	{
		return tries;
	}
	
	public Boolean guess(char letter)
	{
		return false;
	}	
}
