package com.example.hangman.mainactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.hangman.R;
import com.example.hangman.gameplay.EvilGameplay;
import com.example.hangman.gameplay.Gameplay;
import com.example.hangman.gameplay.GameplayListener;
import com.example.hangman.gameplay.GoodGameplay;
import com.example.hangman.graphics.GLRenderer;
import com.example.hangman.graphics.Gallows;
import com.example.hangman.graphics.GameSurfaceView;
import com.example.hangman.highscoreactivity.HighScoreActivity;
import com.example.hangman.history.History;
import com.example.hangman.mainactivity.DialogListener;
import com.example.hangman.mainactivity.WinDialog;
import com.example.hangman.virtualkeyboard.KeyboardListener;
import com.example.hangman.virtualkeyboard.VirtualKeyboard;
import com.example.hangman.settings.Settings;
import com.example.hangman.settingsactivity.*;

public class MainActivity extends Activity implements GameplayListener, KeyboardListener, DialogListener
{
	//private RenderTarget renderTarget;
	private VirtualKeyboard keyboard;
	private Gameplay gameplay;
	private Gallows gallows;
	private History history;
	private GameSurfaceView gameSurfaceView;
	private GLRenderer renderer;
	private Settings settings;
	
	private ArrayList<String> words = new ArrayList<String>();	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {   				
        super.onCreate(savedInstanceState);       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);             
             
        gallows = new Gallows();
        
        gameSurfaceView = (GameSurfaceView) findViewById(R.id.surfaceView1);
        renderer = new GLRenderer(getAssets(), gallows);
        
        gameSurfaceView.setRenderer(renderer);
        
        history = new History(this);
        keyboard = new VirtualKeyboard(this, this);
        settings = new Settings(this);
        loadWords(settings.wordLength);   
        
        Button options = (Button) findViewById(R.id.button1);
        options.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
		    	startActivity(intent);	
			}
		});
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
    	startGame();
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	//renderTarget.pause();
    	gameSurfaceView.onPause();
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	gameSurfaceView.onResume();
    }
            
    // Load previous game settings
    private void load()
    {
    }
    
    private void startGame()
    {
    	keyboard.reset();
        
    	gameplay = settings.isEvil ? new EvilGameplay(words, settings.wordLength, settings.maxTries, this) : 
    		new GoodGameplay(words, settings.wordLength, settings.maxTries, this);    	
    	
    	gallows.setMaxSteps(settings.maxTries);
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
    
    public void onWin(String word, int tries)
    {    
    	WinDialog dialog = new WinDialog();
    	dialog.setListener(this);
    	dialog.setCancelable(false);
    	dialog.show(getFragmentManager(), "Hangman");
    	history.score(word, tries);
    }    
    
    @Override
    public void onHighscoreSelect()
    {    
    	Intent intent = new Intent(this, HighScoreActivity.class);
    	startActivity(intent);
    }
    
    @Override
    public void onNewGame()
    {
    	startGame();    
    } 
    
    private void loadWords(int length)
    {
    	try
    	{
    		InputStream stream = getAssets().open("words" + length + ".txt");
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
