package com.example.hangman;

public interface GameplayListener 
{
	public void onGuess(Boolean correctness, String word);
	public void onLose(String word);
	public void onWin(String word);	
}
