package com.example.hangman.mainactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
        
        history = new History(this);
        
        gallows = new Gallows();
        
        gameSurfaceView = (GameSurfaceView) findViewById(R.id.surfaceView1);
        renderer = new GLRenderer(getAssets(), gallows);
        
        gameSurfaceView.setRenderer(renderer);
        
        settings = new Settings();
        settings.load(this);
        gameState = new GameState();
        gameState.load(this);
        replaySavedGame();
                
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
				startGame();
			}
		});
    }
    
    private void replaySavedGame()
    {
    	Settings s = gameState.settings;
    	
    	loadWords(s.wordLength);
    	
    	if(s.isEvil)
    	{
    		gameplay = new EvilGameplay(words, s.wordLength, s.maxTries, this);
    	}
    	else
    	{
    		GoodGameplay gg = new GoodGameplay(words, s.wordLength, s.maxTries, this);
    		if(!gameState.word.equals("")) gg.word = gameState.word;
    		gameplay = gg;
    	}
    	
    	gallows.setMaxSteps(s.maxTries);
    	
    	for(int i = 0; i < gameState.keyLog.length(); ++i)
    	{
    		onKeyPressed(gameState.keyLog.charAt(i));
    	}
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
    
    private void startGame()
    {
    	keyboard.reset();
    	
    	// Load possible new settings
    	settings.load(this);
    	gameState.reset(settings);
    	
        loadWords(settings.wordLength);
    	
    	gameplay = settings.isEvil ? new EvilGameplay(words, settings.wordLength, settings.maxTries, this) : 
    		new GoodGameplay(words, settings.wordLength, settings.maxTries, this);    	
    	
    	gallows.setMaxSteps(settings.maxTries);
    	gallows.reset();
    	
    	updateProgress();
    }    
            
    private void updateProgress()
    {
    	int maxTries = gameState.settings.maxTries;
    	String guess = gameplay.getGuess();
    	
    	TextView text = (TextView)findViewById(R.id.hangmanProgress);
    	text.setText(guess);
    	TextView tries = (TextView)findViewById(R.id.textViewTries);
    	tries.setText("" + (maxTries - gameplay.getTries()));
    }
    
    public void onKeyPressed(char letter)
    {
    	boolean isCorrect = gameplay.guess(letter);
    	
    	gameState.updateState(letter);
    	
    	keyboard.highlight(letter, isCorrect);

    	audio.stop();
    	if(!isCorrect)
		{ 
    		audio.play(this, AudioManager.HAMMER);
    		gallows.nextStep();
		}
    	else
    	{
    		audio.play(this, AudioManager.CORRECT);
    	}

    	updateProgress();
    }
    
    public void onLose(String word)
    {
    	// reset our game state, don't want to lose again if we load back in
    	gameState.reset(settings);
    	
    	audio.play(this, AudioManager.LOSE);
    	
    	LoseDialog dialog = new LoseDialog(word, this);
    	dialog.show(getFragmentManager(), "Hangman");
    }
    
    public void onWin(String word, int tries)
    {
    	// reset our game state, don't want to lose again if we load back in
    	gameState.reset(settings);
    	
    	audio.play(this, AudioManager.WIN);
    	
    	WinDialog dialog = new WinDialog(word, this);
    	dialog.show(getFragmentManager(), "Hangman");
    	history.score(word, tries);
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
    }
    
    @Override
    public void onNewGameSelect()
    {
    	startGame();    
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
