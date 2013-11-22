package com.example.hangman.audio;

import com.example.hangman.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AudioManager 
{
	private MediaPlayer mp;
	
	public static int WIN = 1;
	public static int LOSE = 2;
	
	public void play(Context context, int music)
	{
		int id = (music == WIN) ? R.raw.winner : R.raw.loser;
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
