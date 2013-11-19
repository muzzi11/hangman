package com.example.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.hangman.VirtualKeyboard;
import com.example.hangman.KeyboardListener;
import com.example.hangman.GameplayListener;
import com.example.hangman.WinDialog;
import com.example.hangman.DialogListener;

public class MainActivity extends Activity implements GameplayListener, KeyboardListener, DialogListener
{
	private RenderTarget renderTarget;
	private VirtualKeyboard keyboard;
	private Gameplay gameplay;
	private Gallows gallows;
	
	private ArrayList<String> words = new ArrayList<String>();	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {   				
        super.onCreate(savedInstanceState);       
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
    	
    	loadWords();
    	    	
    	keyboard = new VirtualKeyboard(this, this);
    	
    	gallows = new Gallows();
    	gallows.loadAssets(getAssets());
    	
    	renderTarget = (RenderTarget) findViewById(R.id.surfaceView1);
    	renderTarget.setGallows(gallows);
    	
    	Log.d("HM", Long.toString(Runtime.getRuntime().maxMemory() / 1024));
    	Log.d("HM", Long.toString(Runtime.getRuntime().totalMemory() / 1024));
    	
    	startGame();
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	renderTarget.pause();
    }
            
    // Load previous game settings
    private void load()
    {
    }
    
    private void startGame()
    {        
        //gameplay = new GoodGameplay(words, 5, 15, this);
    	gameplay = new EvilGameplay(words, 5, 25, this);
    	gallows.setMaxSteps(20);
    	gallows.reset();
    	updateProgress();
    }    
            
    private void updateProgress()
    {
    	String guess = gameplay.getGuess();
    	TextView text = (TextView)findViewById(R.id.hangmanProgress);
    	text.setText(guess);
    }
    
    public void onKeyPressed(char letter)
    {
    	boolean isCorrect = gameplay.guess(letter);
    	keyboard.highlight(letter, isCorrect);
    	if(!isCorrect) gallows.nextStep();
    	updateProgress();
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
    
    private void loadWords()
    {
    	try
    	{
    		InputStream stream = getApplicationContext().getAssets().open("words.txt");
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		String line;
    		while((line = bufferedReader.readLine()) != null)
    		{
    			words.add(line);
    		}
    		
    		bufferedReader.close();
    	}
    	catch(IOException e)
    	{
    		Log.e("loadWords", e.getMessage());
    	}
    }
}
