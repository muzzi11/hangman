package com.example.hangman.gameplay;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.util.SparseIntArray;

public class EvilGameplay extends Gameplay
{
    private ArrayList<String> words;
    
    public EvilGameplay(ArrayList<String> words, int length, int tries, GameplayListener listener)
    {
            super(length, tries, listener);                
            
            ArrayList<String> filteredWords = new ArrayList<String>();
            for (String word : words)                
                    if (word.length() == length) filteredWords.add(word);
            this.words = filteredWords;
    }
    
    @Override
    public boolean guess(char letter)        
    {                 
    	boolean containsLetter;
        int c = getMostFrequentClass(words, letter);        
        
        // Filter all words that are not of the same class
        ArrayList<String> newWords = new ArrayList<String>();
        for(String word : words)        
            if(classifyWord(word, letter) == c) newWords.add(word);
        
        words = newWords;
        containsLetter = c > 0;
        if (containsLetter)
        {
	        // Update the guess
	        StringBuilder updatedGuess = new StringBuilder(guess);
	        for (int i = 0; c != 0; c = c >> 1, ++i)
	        {
                if((c & 1) > 0) updatedGuess.setCharAt(guess.length() - 1 - i, letter);
	        }
	        guess = updatedGuess.toString();
        }
        else                	
            ++tries;                

        if(won()) listener.onWin(guess, tries);
        else if(lost()) listener.onLose(chooseRandomWord());
        
        return containsLetter;
    }
    
    /*
     *  Returns an integer that represents the class of a word for a specific letter.
     *  The class of a word for a specific letter can be seen as the count and positions
     *  of the letter occurring in that word.
     */
    private int classifyWord(String word, char letter)
    {
        int c = 0;
        
        for(int i = 0; i < word.length(); ++i)
        {
            c = c << 1;
            if(word.charAt(i) == letter) c |= 1;
        }        
        return c;
    }
    
    private int getMostFrequentClass(ArrayList<String> samples, char letter)
    {
        SparseIntArray classes = new SparseIntArray();
        
        // Count frequencies of classes
        for(String sample : samples)
        {
                int c = classifyWord(sample, letter);
                Integer freq = classes.get(c);
                classes.put(c, freq + 1);
        }
        
        // Find highest frequency
        int key = 0, maxFreq = 0;
        for(int i = 0; i < classes.size(); ++i)
        {
            if(classes.valueAt(i) > maxFreq)
            {
                key = classes.keyAt(i);
                maxFreq = classes.valueAt(i);
            }
        }
        
        return key;
    }
    
    private String chooseRandomWord()
	{
		Random generator = new Random();
		int index = generator.nextInt(words.size());
		return words.get(index).toUpperCase(Locale.UK);	
	}

}