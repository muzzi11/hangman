package com.example.hangman.highscoreactivity;

import java.util.ArrayList;

import com.example.hangman.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HighScoreActivity extends Activity {

	private ArrayList<String> highscores;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);	
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.highscore);
	    
	    highscores = new ArrayList<String>();
	    
	    Intent intent = getIntent();
	    ArrayList<String> words = intent.getStringArrayListExtra("words");
	    ArrayList<Integer> scores = intent.getIntegerArrayListExtra("scores");
	    
	    // Build a formatted list of highscores with position, word and score
	    for(int i = 0; i < words.size(); ++i)
	    {
	    	StringBuilder builder = new StringBuilder();
	    	builder.append(i + 1).append(".\t");
	    	builder.append(words.get(i)).append('\t');
	    	builder.append(scores.get(i));
	    	highscores.add(builder.toString());
	    }
		
		ListView listView = (ListView) findViewById(R.id.scores);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.score, highscores);
		listView.setAdapter(arrayAdapter);
	}
}
