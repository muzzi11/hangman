package com.example.hangman.audio;

import com.example.hangman.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class AudioManager 
{
	private MediaPlayer mp;
	
	public static final int WIN = 1;
	public static final int LOSE = 2;
	public static final int HAMMER = 3;
	public static final int CORRECT = 4;
	
	private boolean released = false;
	
	public void play(Context context, int music)
	{
		int id = 1;
		
		stop();
		Log.d("Hangman", "Playing " + music);
		switch (music)
		{
			case WIN:
				id = R.raw.winner;
				break;
			case LOSE:
				id = R.raw.loser;
				break;
			case HAMMER:
				id = R.raw.hammer;
				break;
			case CORRECT:
				id = R.raw.correct;
				break;
		}		
		mp = MediaPlayer.create(context,id);
		
		mp.setOnCompletionListener(new OnCompletionListener() 
		{			
			@Override
			public void onCompletion(MediaPlayer mp) {
				stop();				
			}
		});
        mp.start();
        released = false;
	}
	
	public void stop()
	{		
		Log.d("Hangman", "Stopping");
		if (mp == null) return;
				
		if (!released) mp.stop();
				
		mp.release();
		released = true;
	}
}
