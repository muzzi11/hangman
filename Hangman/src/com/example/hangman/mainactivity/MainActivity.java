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
	//private RenderTarget renderTarget;
	private VirtualKeyboard keyboard;
	private Gameplay gameplay;
	private Gallows gallows;
	private History history;
	private GameSurfaceView gameSurfaceView;
	private GLRenderer renderer;
	private Settings settings;
	private AudioManager audio;
	
	private int wordLength;
	private int maxTries;
	private boolean isEvil;
	private boolean isFinished;
	
	private ArrayList<String> words;	
	
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
        audio = new AudioManager();
        
        ImageButton settings = (ImageButton) findViewById(R.id.settings);
        settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
		    	startActivity(intent);	
			}
		});
        
        ImageButton newGame = (ImageButton) findViewById(R.id.newGame);
        newGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startGame();
			}
		});
        
        startGame();
    }  
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	//renderTarget.pause();
    	gameSurfaceView.onPause();
    	
    	if (!isFinished) audio.stop();
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
    	isFinished = false;
    	
    	keyboard.reset();
        
    	settings = new Settings(this);
    	wordLength = settings.wordLength;
    	maxTries = settings.maxTries;
    	isEvil = settings.isEvil;
    	
        loadWords(wordLength);
    	
    	gameplay = isEvil ? new EvilGameplay(words, wordLength, maxTries, this) : 
    		new GoodGameplay(words, wordLength, maxTries, this);    	
    	
    	gallows.setMaxSteps(maxTries);
    	gallows.reset();
    	
    	updateProgress();
    }    
            
    private void updateProgress()
    {
    	String guess = gameplay.getGuess();
    	TextView text = (TextView)findViewById(R.id.hangmanProgress);
    	text.setText(guess);
    	TextView tries = (TextView)findViewById(R.id.textViewTries);
    	tries.setText("" + (maxTries - gameplay.getTries()));
    }
    
    public void onKeyPressed(char letter)
    { 	
    	boolean isCorrect = gameplay.guess(letter);    	    	    	
    	keyboard.highlight(letter, isCorrect);    	
   	    	
    	if(!isCorrect)
		{ 
    		if (!isFinished) audio.play(this, AudioManager.HAMMER);
    		gallows.nextStep();
		}
    	else 
    		if (!isFinished) audio.play(this, AudioManager.CORRECT);
    	updateProgress();
    }
    
    public void onLose(String word)
    {       	
    	isFinished = true;
    	audio.play(this, AudioManager.LOSE);
    	    	
    	LoseDialog dialog = new LoseDialog();
    	dialog.word = word;
    	dialog.listener = this;    	
    	dialog.setCancelable(false);
    	dialog.show(getFragmentManager(), "Hangman");    	    	
    }
    
    public void onWin(String word, int tries)
    {   
    	isFinished = true;
    	audio.play(this, AudioManager.WIN);    	
    	
    	WinDialog dialog = new WinDialog();    	
    	dialog.word = word;
    	dialog.listener = this;    	
    	dialog.setCancelable(false);
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
    public void onNewGame()
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
