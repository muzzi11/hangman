package com.example.hangman;

public interface GameplayListener 
{
	public void onGuess(Boolean success, char letter);
	public void onLose(String word);
	public void onWin(String word);	
}
