package com.example.hangman;

import java.util.Map;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

public class VirtualKeyboard
{	
	private Map<Character, Button> buttons;
	private Drawable correct;
	private Drawable incorrect;
	private Drawable normal;
	
	public VirtualKeyboard(Map<Character, Button> buttons, Drawable[] buttonStates)
	{
		this.correct = buttonStates[0];
		this.incorrect = buttonStates[1];
		this.normal = buttonStates[2];
		this.buttons = buttons;
		
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
	}
	
	private void Highlight(char letter)
	{
		Button button = buttons.get(letter);
		
		//button.setBackgroundDrawable(correct);
		//button.setBackgroundDrawable(incorrect);
	}
	
	private void Reset()
	{
		for (Button button : buttons.values())
		{
			button.setBackgroundDrawable(normal);
			button.setEnabled(true);
		}
	}
}
