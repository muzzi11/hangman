package com.example.hangman;

import java.util.ArrayList;
import java.util.Vector;

public class Gameplay 
{
	public String guess;
	public GameplayListener listener;
	public int maxTries;
	public int tries;	
	public int length;
	
	public ArrayList<String> words;
	
	Gameplay(ArrayList<String> words, int length, int maxTries, GameplayListener listener)
	{
		this.words = words;
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
