package com.example.hangman.graphics;

public class Gallows implements SpriteBatchRenderer
{
	private Object lock = new Object();
	private int currentStep = 0, maxSteps;
	
	public void setMaxSteps(int max)
	{
		synchronized (lock)
		{
			maxSteps = max;
		}
	}
	
	public void nextStep()
	{
		synchronized (lock)
		{
			++currentStep;
			if(currentStep > maxSteps) currentStep = maxSteps;
		}
	}
	
	public void reset()
	{
		synchronized (lock)
		{
			currentStep = 0;
		}
	}
	
	@Override
	public void draw(Sprite[] sprites)
	{
		synchronized (lock)
		{
			if(currentStep > 0)
			{
				int index = Math.round(25 * currentStep / (float)maxSteps) - 1;
				sprites[index].draw();
			}
		}
	}
}
