package com.example.hangman;

import java.util.HashMap;
import java.util.Map;

import android.widget.Button;

public class VirtualKeyboard
{
	private Map<Character, Button> buttons;

	public VirtualKeyboard()
	{
		buttons = new HashMap<Character, Button>();
		
	}
}
