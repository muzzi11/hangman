package com.example.hangman;

import java.util.Vector;

public class Gameplay 
{
	public String guess;
	public GameplayListener listener;
	public int maxTries;
	public int tries;	
	public int length;
	
	public Vector<String> words;
	
	Gameplay(Vector<String> words, int length, int maxTries, GameplayListener listener)
	{
		this.words = words;
		this.length = length;
		this.maxTries = maxTries;		
		this.listener = listener;
		
		tries = 0;
		
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
	
	public void guess(char letter)
	{		
	}	
	
	public Boolean finished()
	{
		return !guess.contains("_");
	}
	
	public Boolean lost()
	{
		return tries >= maxTries;
	}
}
