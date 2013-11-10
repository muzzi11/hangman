package com.example.hangman;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;

import com.example.hangman.VirtualKeyboard;
import com.example.hangman.GameplayListener;

public class MainActivity extends Activity implements GameplayListener 
{
	private VirtualKeyboard keyboard;
	private Vector<String> words = new Vector<String>();
	
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
    	
    	loadWords();
    	
    	Resources resources = getResources();
    	Drawable[] drawables = new Drawable[3];
    	drawables[0] = resources.getDrawable(R.drawable.correct_guess);
    	drawables[1] = resources.getDrawable(R.drawable.incorrect_guess);
    	drawables[2] = resources.getDrawable(android.R.drawable.bottom_bar);    	
    	keyboard = new VirtualKeyboard(GetKeyboardButtons(), drawables);    	
    }
        
    private HashMap<Character, Button> GetKeyboardButtons()
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
    
    public void onGuess(Boolean correctness, String word)
    {
    }
    
    public void onLose(String word)
    {    
    }
    
    public void onWin(String word)
    {    	
    }    
    
    private void loadWords()
    {
    	int eventType;
    	XmlPullParser parser = Xml.newPullParser();
    	InputStream stream = getApplicationContext().getAssets().open("data/words.xml");
    	
    	parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
    	parser.setInput(stream, null);
    	
    	eventType = parser.getEventType();
    	while(eventType != XmlPullParser.END_DOCUMENT)
    	{
    		if(eventType == XmlPullParser.START_TAG)
    		{
    			if(parser.getName() == "item")
    			{
    				String word = parser.nextText();
    				words.add(word);
    			}
    		}
    		
    		eventType = parser.next();
    	}
    }
}
