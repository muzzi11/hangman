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
import android.widget.ImageButton;
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
import com.example.hangman.history.HistoryEntry;
import com.example.hangman.mainactivity.DialogListener;
import com.example.hangman.mainactivity.WinDialog;
import com.example.hangman.virtualkeyboard.KeyboardListener;
import com.example.hangman.virtualkeyboard.VirtualKeyboard;
import com.example.hangman.settings.Settings;
import com.example.hangman.settingsactivity.*;
import com.example.hangman.audio.*;

public class MainActivity extends Activity implements GameplayListener, KeyboardListener, DialogListener
{
	private ArrayList<String> words;	
	
	private VirtualKeyboard keyboard;
	
	private History history;
	
	private Gameplay gameplay;
	
	private Gallows gallows;
	
	private GameSurfaceView gameSurfaceView;
	private GLRenderer renderer;
	
	private Settings settings;
	private GameState gameState;

	private AudioManager audio;	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {   				
        super.onCreate(savedInstanceState);       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);             
        
        words = new ArrayList<String>();
        
        keyboard = new VirtualKeyboard(this, this);
        audio = new AudioManager();
        
        history = new History(this);
        
        gallows = new Gallows();
        
        gameSurfaceView = (GameSurfaceView) findViewById(R.id.surfaceView1);
        renderer = new GLRenderer(getAssets(), gallows);
        
        gameSurfaceView.setRenderer(renderer);
        
        settings = new Settings();
        settings.load(this);
        gameState = new GameState();
        gameState.load(this);

        startGame(true);
                
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
		    	startActivity(intent);
			}
		});
        
        ImageButton newGameButton = (ImageButton) findViewById(R.id.newGame);
        newGameButton.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				startGame(false);
			}
		});
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	if(!gameState.settings.isEvil)
    	{
    		GoodGameplay gg = (GoodGameplay) gameplay;
    		gameState.word = gg.word;
    	}
    	gameState.save(this);
    	gameSurfaceView.onPause();
    	
    	audio.stop();
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	gameSurfaceView.onResume();
    }
    
    private void startGame(boolean replaySaveGame)
    {    	    	
    	keyboard.reset();
    	audio.stop();
    	
    	if(!replaySaveGame)
    	{
	    	// Load possible new settings
	    	settings.load(this);
	    	gameState.reset(settings);
    	}
    	
    	loadWords(settings.wordLength);
        
        if(gameState.settings.isEvil)
        {
                gameplay = new EvilGameplay(words, settings.wordLength, settings.maxTries, this);
        }
        else
        {
                GoodGameplay gg = new GoodGameplay(words, settings.wordLength, settings.maxTries, this);
                // restore the randomly picked word from the save game
                if(!gameState.word.equals("")) gg.word = gameState.word;
                gameplay = gg;
        }
        
        gallows.setMaxSteps(settings.maxTries);
        gallows.reset();
        
        if(replaySaveGame)
        {
        	audio.mute(true);
        	for(int i = 0; i < gameState.keyLog.length(); ++i)
        	{
        		onKeyPressed(gameState.keyLog.charAt(i));
        	}
        	audio.mute(false);
        }
    	
    	updateProgress();
    }    
            
    private void updateProgress()
    {
    	int maxTries = gameState.settings.maxTries;
    	Log.d("Hangman", "GameState maxtries " + maxTries);
    	String guess = gameplay.getGuess();
    	
    	TextView text = (TextView)findViewById(R.id.hangmanProgress);
    	text.setText(guess);
    	TextView tries = (TextView)findViewById(R.id.textViewTries);
    	tries.setText("" + (maxTries - gameplay.getTries()));
    }
    
    @Override
    public void onKeyPressed(char letter)
    { 	
    	boolean isCorrect = gameplay.guess(letter);    	    	    	
    	keyboard.highlight(letter, isCorrect);
    	gameState.updateState(letter);

    	if(!isCorrect)
		{ 
    		audio.play(this, AudioManager.HAMMER);
    		gallows.nextStep();
		}
    	else 
    		audio.play(this, AudioManager.CORRECT);

    	updateProgress();
    }
    
    @Override
    public void onLose(String word)
    {       	
    	audio.play(this, AudioManager.LOSE);
    	gameState.reset(settings);
    	
    	LoseDialog dialog = new LoseDialog(this, word);
    	dialog.show(getFragmentManager(), "Hangman");    	    	
    }
    
    @Override
    public void onWin(String word, int tries)
    {
    	int score = history.score(word, tries, gameplay.getMaxTries());
    	audio.play(this, AudioManager.WIN);    	
    	gameState.reset(settings);
    	
    	WinDialog dialog = new WinDialog(this, word, score);    	
    	dialog.show(getFragmentManager(), "Hangman");    	
    }    
    
    @Override
    public void onHighscoreSelect()
    {
    	Intent intent = new Intent(this, HighScoreActivity.class);
    	ArrayList<String> words = new ArrayList<String>();
    	ArrayList<Integer> scores = new ArrayList<Integer>();
    	ArrayList<HistoryEntry> entries = history.getEntries();
    	
    	for(HistoryEntry entry : entries)
    	{
    		words.add(entry.word);
    		scores.add(entry.score);
    	}
    	
    	intent.putStringArrayListExtra("words", words);
    	intent.putIntegerArrayListExtra("scores", scores);
    	
    	startActivity(intent);
    	startGame(false);
    }
    
    @Override
    public void onNewGameSelect()
    {
    	startGame(false);
    } 
    
    private void loadWords(int length)
    {
    	words = new ArrayList<String>();
    	try
    	{
    		Log.d("Hangman", "Opening file: " + "words" + length + ".txt");
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
