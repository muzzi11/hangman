package com.example.hangman;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.TextView;

import com.example.hangman.VirtualKeyboard;
import com.example.hangman.KeyboardListener;
import com.example.hangman.GameplayListener;

public class MainActivity extends Activity implements GameplayListener, KeyboardListener
{
	private VirtualKeyboard keyboard;
	private Vector<String> words = new Vector<String>();
	private Gameplay gameplay;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {   				
        super.onCreate(savedInstanceState);
        
        String[] words = new String[2];
        words[0] = "appel";
        words[1] = "lingo";
        
        gameplay = new GoodGameplay(words, 5, 25, this);
        
        setContentView(R.layout.activity_main);        
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
    	
    	loadWords();
    	
    	Resources resources = getResources();
    	Drawable[] drawables = new Drawable[3];
    	drawables[0] = resources.getDrawable(R.drawable.correct_guess);
    	drawables[1] = resources.getDrawable(R.drawable.incorrect_guess);
    	drawables[2] = resources.getDrawable(android.R.drawable.bottom_bar);
    	keyboard = new VirtualKeyboard(getKeyboardButtons(), drawables, this);
    	
    	updateProgess();    	
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
    
    private void updateProgess()
    {
    	String guess = gameplay.getGuess();
    	TextView text = (TextView)findViewById(R.id.hangmanProgress);
    	text.setText(guess);
    }
    
    public void onGuess(Boolean correctness, String word)
    {
    }
    
    public void onLose(String word)
    {    
    }
    
    public void onWin(String word)
    {    	
    }
    
    public void keyPressed(char letter)
    {
    	Boolean success = gameplay.guess(letter);
    	keyboard.highlight(letter, success);
    	updateProgess();
    }   
    
    private void loadWords()
    {
    	int eventType;
    	XmlPullParser parser = Xml.newPullParser();
    	try
    	{
	    	InputStream stream = getApplicationContext().getAssets().open("words.xml");
	    	
	    	parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	    	parser.setInput(stream, null);
	    	
	    	eventType = parser.getEventType();
	    	while(eventType != XmlPullParser.END_DOCUMENT)
	    	{
	    		if(eventType == XmlPullParser.START_TAG)
	    		{
	    			if(parser.getName().equals("item"))
	    			{
	    				String word = parser.nextText();
	    				words.add(word);
	    			}
	    		}
	    		
	    		eventType = parser.next();
	    	}
    	}
    	catch(XmlPullParserException e)
    	{
    		Log.e("loadWords", e.getMessage());
    	}
    	catch(IOException e)
    	{
    		Log.e("loadWords", e.getMessage());
    	}
    } 
}
