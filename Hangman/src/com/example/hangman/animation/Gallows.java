package com.example.hangman.animation;

import android.content.res.AssetManager;
import android.graphics.Canvas;

public class Gallows
{
	private int currentStep = 0, maxSteps;
	private Sprite[] sprites = new Sprite[25];
	
	public void loadAssets(AssetManager assetManager)
	{
		for(int i = 0; i < 25; ++i)
		{
			String name = "gallows/" + Integer.toString(i+1) + ".png";
			sprites[i] = Sprite.loadFromAsset(assetManager, name, 0, 0, 0.8f);
		}
	}
	
	public void setMaxSteps(int max)
	{
		synchronized (sprites)
		{
			maxSteps = max;
		}
	}
	
	public void nextStep()
	{
		synchronized (sprites)
		{
			++currentStep;
			if(currentStep > maxSteps) currentStep = maxSteps;
		}
	}
	
	public void reset()
	{
		synchronized (sprites)
		{
			currentStep = 0;
		}
	}
	
	public void draw(Canvas canvas)
	{
		synchronized (sprites)
		{
			if(currentStep > 0)
			{
				int index = Math.round(25 * currentStep / (float)maxSteps) - 1;
				sprites[index].draw(canvas);
			}
		}
	}
}
