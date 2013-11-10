package com.example.hangman;

public class Gameplay 
{
	private String guess;
	private GameplayListener listener;
	private int tries;
	private int maxTries;
	private int length;
	
	private String[] words;
	
	Gameplay(String[] words, int length, int maxTries, GameplayListener listener)
	{
		this.words = words;
		this.length = length;
		this.tries = tries;
		this.listener = listener;	
	}
	
	public String getGuess()
	{
		return guess;
	}
	
	public int GetMaxTries()
	{
		return tries;
	}
	
	public 
}
