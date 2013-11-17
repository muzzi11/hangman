package com.example.hangman;

public class Gameplay 
{
	protected String guess;
	protected GameplayListener listener;
	protected int maxTries;
	protected int tries;	
	protected int length;
	
	Gameplay(int length, int maxTries, GameplayListener listener)
	{
		this.length = length;
		this.maxTries = maxTries;		
		this.listener = listener;
		
		tries = 0;
		
		guess = "";
		for (int i = 0; i < length; i++)
			guess += "-";
	}
	
	public String getGuess()
	{
		return guess;
	}
	
	public int getMaxTries()
	{
		return tries;
	}
	
	public void guess(char letter)
	{		
	}	
	
	public Boolean finished()
	{
		return !guess.contains("-");
	}
	
	public Boolean lost()
	{
		return tries >= maxTries;
	}
}
