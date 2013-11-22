package com.example.hangman.audio;

import com.example.hangman.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AudioManager 
{
	private MediaPlayer mp;
	
	public static final int WIN = 1;
	public static final int LOSE = 2;
	public static final int HAMMER = 3;
	public static final int CORRECT = 4;
	
	public void play(Context context, int music)
	{
		int id = 1;
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
		mp = MediaPlayer.create(context, id);
		
		mp.setOnCompletionListener(new OnCompletionListener() 
		{			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();				
			}
		});
        mp.start();
	}
	
	public void stop()
	{
		if (mp != null) mp.release();
	}
}
