package com.example.hangman.animation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class Sprite
{
	private Bitmap bitmap;
	private Rect src;
	private RectF dst;
	
	public Sprite(Bitmap bitmap, int x, int y, float scale)
	{
		this.bitmap = bitmap;
		float scaledWidth = bitmap.getWidth() * scale;
		float scaledHeight = Math.round(bitmap.getHeight()*scale);
		src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		dst = new RectF(x, y, x + scaledWidth, y + scaledHeight);
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(bitmap, src, dst, null);
	}
	
	static public Sprite loadFromAsset(AssetManager assetManager, String name, int x, int y, float scale)
	{
		InputStream in;
		Bitmap bitmap = null;
		
		try
		{
			in = assetManager.open(name);
			bitmap = BitmapFactory.decodeStream(in);
		}
		catch (IOException e)
		{
			Log.e("Sprite", e.getMessage());
		}
		
		return bitmap != null ? new Sprite(bitmap, x, y, scale) : null; 
	}
}
