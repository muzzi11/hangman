package com.example.hangman.gameplay;

public interface GameplayListener 
{
	public void onLose(String word);
	public void onWin(String word, int tries);	
}
