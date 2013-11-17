package com.example.hangman;

public interface GameplayListener 
{
	public void onLose(String word);
	public void onWin(String word);	
}
