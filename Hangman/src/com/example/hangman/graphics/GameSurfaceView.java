package com.example.hangman.graphics;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GameSurfaceView extends GLSurfaceView
{
	public GameSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		
		// Make surface translucent
		setEGLConfigChooser(8, 8, 8, 8, 0, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
		setPreserveEGLContextOnPause(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		return true;
	}
}
