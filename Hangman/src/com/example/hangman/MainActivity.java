package com.example.hangman;

import java.util.HashMap;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.hangman.VirtualKeyboard;
import com.example.hangman.KeyboardListener;
import com.example.hangman.GameplayListener;
import com.example.hangman.WinDialog;
import com.example.hangman.DialogListener;

public class MainActivity extends Activity implements GameplayListener, KeyboardListener, DialogListener
{
	private VirtualKeyboard keyboard;
	private Gameplay gameplay;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {   				
        super.onCreate(savedInstanceState);       
       
        setContentView(R.layout.activity_main);        
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
    	
    	Resources resources = getResources();
    	Drawable[] drawables = new Drawable[3];
    	drawables[0] = resources.getDrawable(R.drawable.correct_guess_selector);
    	drawables[1] = resources.getDrawable(R.drawable.incorrect_guess);
    	drawables[2] = resources.getDrawable(android.R.drawable.bottom_bar);
    	keyboard = new VirtualKeyboard(getKeyboardButtons(), drawables, this);
    	
    	startGame();
    }
    
    private HashMap<Character, Button> getKeyboardButtons()
    {
    	HashMap<Character, Button> buttons = new HashMap<Character, Button>();
    	for (char letter = 'A'; letter <= 'Z'; letter++)
		{    		    		
			int resourceID = getResources().getIdentifier("Key" + letter, "id", getPackageName());			
			Button button = (Button) findViewById(resourceID);			
			buttons.put(letter, button);			
		}    	
        return buttons;
    }
    
    // Load previous game settings
    private void load()
    {
    }
    
    private void startGame()
    {
        String[] words = new String[2];
        words[0] = "appel";
        words[1] = "lingo";
        
        gameplay = new GoodGameplay(words, 5, 5, this);
        
    	updateProgess();
    }    
            
    private void updateProgess()
    {
    	String guess = gameplay.getGuess();
    	TextView text = (TextView)findViewById(R.id.hangmanProgress);
    	text.setText(guess);
    }
    
    public void onKeyPressed(char letter)
    {
    	gameplay.guess(letter);    	
    }   
    
    public void onGuess(Boolean success, char letter)
    {
    	keyboard.highlight(letter, success);
    	updateProgess();
    }
    
    public void onLose(String word)
    {    
    	LoseDialog dialog = new LoseDialog();
    	dialog.setListener(this);
    	dialog.setCancelable(false);
    	dialog.show(getFragmentManager(), "Hangman");
    }
    
    public void onWin(String word)
    {    
    	WinDialog dialog = new WinDialog();
    	dialog.setListener(this);
    	dialog.setCancelable(false);
    	dialog.show(getFragmentManager(), "Hangman");
    }    
    
    public void onHighscoreSelect()
    {    
    }
    
    public void onNewGame()
    {
    	keyboard.reset();
    	startGame();    
    }
}
