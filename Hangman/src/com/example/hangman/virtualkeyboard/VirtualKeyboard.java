package com.example.hangman.virtualkeyboard;

import java.util.HashMap;
import java.util.Map;

import com.example.hangman.R;
import com.example.hangman.R.drawable;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class VirtualKeyboard
{	
	private Map<Character, Button> buttons;
	private Drawable correct;
	private Drawable incorrect;
	private Drawable normal;
	private KeyboardListener listener;
	
	public VirtualKeyboard(Activity activity, KeyboardListener listenerIn)
	{	    	
		Resources resources = activity.getResources();
    	correct = resources.getDrawable(R.drawable.correct_guess_selector);
    	incorrect = resources.getDrawable(R.drawable.incorrect_guess);
    	normal = resources.getDrawable(android.R.drawable.bottom_bar);
    	buttons = getKeyboardButtons(activity);
    	listener = listenerIn;
    	
		for (Button button : buttons.values())
		{
			button.setOnClickListener(new View.OnClickListener() 
			{				
				@Override
				public void onClick(View view)
				{					 
					onTouch(view);
				}
			});
		}
	}
	
	private void onTouch(View view)
	{
		Button button = (Button)view;
		button.setBackgroundDrawable(correct);
		button.setEnabled(false);
		
		char letter = button.getText().charAt(0);
		listener.onKeyPressed(letter);
	}
	
	public void highlight(char letter, boolean isCorrect)
	{
		Button button = buttons.get(letter);
		
		if (isCorrect)
			button.setBackgroundDrawable(correct);
		else
			button.setBackgroundDrawable(incorrect);
	}
	
	public void reset()
	{
		for (Button button : buttons.values())
		{
			button.setBackgroundDrawable(normal);
			button.setEnabled(true);
		}
	}
	
	private HashMap<Character, Button> getKeyboardButtons(Activity activity)
    {
		Resources resources = activity.getResources();
    	HashMap<Character, Button> buttons = new HashMap<Character, Button>();
    	
    	for (char letter = 'A'; letter <= 'Z'; letter++)
		{    		    		
			int resourceID = resources.getIdentifier("Key" + letter, "id", activity.getPackageName());			
			Button button = (Button) activity.findViewById(resourceID);			
			buttons.put(letter, button);			
		}    	
        return buttons;
    }
}
