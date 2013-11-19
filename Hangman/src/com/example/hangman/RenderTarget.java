package com.example.hangman;

import java.util.concurrent.atomic.AtomicBoolean;

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
		private AtomicBoolean run = new AtomicBoolean(false);
		private int width, height;
		
		public RenderThread(SurfaceHolder surfaceHolder, RenderTarget renderTarget)
		{
			this.surfaceHolder = surfaceHolder;
			this.renderTarget = renderTarget;
		}
		
		public void setRunning(boolean state)
		{
			run.set(state);
		}
		
		@Override
		public void run()
		{
			Canvas canvas;
			
			while(run.get())
			{
				canvas = null;
				
				synchronized (surfaceHolder)
				{
					if(!surfaceHolder.getSurface().isValid()) return;
					
					try
					{
						canvas = surfaceHolder.lockCanvas();
						
						canvas.drawColor(Color.BLUE);
						
						if(renderTarget.gallows != null)
						{
							renderTarget.gallows.draw(canvas);
						}
						
						renderTarget.postInvalidate();
					}
					finally
					{
						if(canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
					}
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
	private Gallows gallows = null;
	
	public RenderTarget(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		getHolder().addCallback(this);
		thread = new RenderThread(getHolder(), this);
	}
	
	public void setGallows(Gallows gallows)
	{
		synchronized (thread.surfaceHolder)
		{
			this.gallows = gallows;
		}
	}
	
	public void pause()
	{
		thread.setRunning(false);
		
		try
		{
			thread.join();
		}
		catch(InterruptedException e)
		{
			Log.e("RenderTarget", e.getMessage());
		}
	}
	
	public void resume()
	{
		thread.setRunning(true);
		thread.start();
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
		resume();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		pause();
	}
}
