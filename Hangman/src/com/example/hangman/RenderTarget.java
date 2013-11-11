package com.example.hangman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RenderTarget extends SurfaceView implements SurfaceHolder.Callback
{
	public class RenderThread extends Thread
	{
		private SurfaceHolder surfaceHolder;
		private RenderTarget renderTarget;
		private boolean run = false;
		private int width, height;
		
		public RenderThread(SurfaceHolder surfaceHolder, RenderTarget renderTarget)
		{
			this.surfaceHolder = surfaceHolder;
			this.renderTarget = renderTarget;
		}
		
		public void setRunning(boolean state)
		{
			run = state;
		}
		
		@Override
		public void run()
		{
			Canvas canvas;
			
			while(run)
			{
				canvas = null;
				
				try
				{
					canvas = surfaceHolder.lockCanvas();
					synchronized (surfaceHolder)
					{
						canvas.drawColor(Color.BLUE);
						canvas.drawCircle(50, 50, 50, new Paint());
						renderTarget.postInvalidate();
					}
				}
				finally
				{
					if(canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		
		public void setSurfaceDimensions(int width, int height)
		{
			synchronized (surfaceHolder)
			{
				this.width = width;
				this.height = height;
			}
		}
	}
	
	private RenderThread thread;
	
	public RenderTarget(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		thread.setSurfaceDimensions(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		setWillNotDraw(false);
		thread = new RenderThread(holder, this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		try
		{
			thread.setRunning(false);
			thread.join();
		}
		catch(InterruptedException e)
		{
			Log.e("RenderTarget", e.getMessage());
		}
	}
}
