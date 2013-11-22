package com.example.hangman.highscoreactivity;

import com.example.hangman.history.History;
import com.example.hangman.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HighScoreActivity extends Activity {

	private History history;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);	
	    setContentView(R.layout.highscore);
	    
	    history = new History(this);
		
		ListView listView = (ListView) findViewById(R.id.scores);
		ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.score, history.scores);
		listView.setAdapter(arrayAdapter);
	}
}
