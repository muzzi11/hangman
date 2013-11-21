package com.example.hangman.highscoreactivity;

import com.example.hangman.history.History;
import com.example.hangman.mainactivity.MainActivity;

import com.example.hangman.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HighScoreActivity extends Activity {

	private History history;
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		history = new History(this);
		
		ListView listView = (ListView) findViewById(R.id.scores);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.score, history.scores);
		listView.setAdapter(arrayAdapter);
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);	
	    setContentView(R.layout.highscore);
	}
	
	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
	}

}
