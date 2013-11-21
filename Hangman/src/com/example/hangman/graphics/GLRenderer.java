package com.example.hangman.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

public class GLRenderer implements GLSurfaceView.Renderer
{
	private static AssetManager assetManager;
	
	private int vertexShader, fragmentShader, program;
	private int mvpMatrixHandle;
	
	private float[] projMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] mvpMatrix = new float[16];
	
	private Sprite[] gallowsSprites = new Sprite[25];
	private SpriteBatchRenderer gallowsBatch;
	
	public GLRenderer(final AssetManager assetManager, SpriteBatchRenderer gallowsBatch)
	{
		GLRenderer.assetManager = assetManager;
		this.gallowsBatch = gallowsBatch;
	}
	
	public static int loadShader(int type, String shaderCode)
	{
	    int shader = GLES20.glCreateShader(type);
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
	
	public static int loadShaderFromAsset(int type, final String filename)
	{
		try
    	{
    		InputStream stream = assetManager.open(filename);
    		InputStreamReader inputStreamReader = new InputStreamReader(stream);
    		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		
    		String line;
    		StringBuilder body = new StringBuilder();
    		
    		while((line = bufferedReader.readLine()) != null)
    		{
    			body.append(line);
    			body.append('\n');
    		}
    		
    		bufferedReader.close();
    		
    		return loadShader(type, body.toString());
    	}
    	catch(IOException e)
    	{
    		Log.e("loadShader", e.getMessage());
    		return -1;
    	}
	}
	
	public static int loadTextureFromAsset(final String filename)
	{ 
	    InputStream in;
		Bitmap bitmap = null;
		final int[] textureHandle = new int[1];
        
        GLES20.glGenTextures(1, textureHandle, 0);
        
		try
		{
			in = assetManager.open(filename);
			bitmap = BitmapFactory.decodeStream(in);
			
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	 
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	 
	        bitmap.recycle();
		}
		catch (IOException e)
		{
			Log.e("loadTexture", e.getMessage());
		}
	 
	    return textureHandle[0];
	}
	
	@Override
	public void onDrawFrame(GL10 unused)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		GLES20.glUseProgram(program);
		
		Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, viewMatrix, 0);
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
		
		gallowsBatch.draw(gallowsSprites);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		GLES20.glViewport(0, 0, width, height);
		float fraction = width / (float) height;
		Matrix.orthoM(projMatrix, 0, -fraction, fraction, -1, 1, -1, 1);
		Matrix.setIdentityM(viewMatrix, 0);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config)
	{
		GLES20.glClearColor(0.2f, 0.2f, 0.3f, 1.0f);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_DEPTH_WRITEMASK);
		GLES20.glDisable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Load and compile vertex and fragment shader
		vertexShader = loadShaderFromAsset(GLES20.GL_VERTEX_SHADER, "shaders/default.vert");
		fragmentShader = loadShaderFromAsset(GLES20.GL_FRAGMENT_SHADER, "shaders/default.frag");
		program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);
		GLES20.glLinkProgram(program);
		
		mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
		
		// Load gallows sprites
		for(int i = 0; i < 25; ++i)
		{
			String name = "gallows/" + Integer.toString(i+1) + ".png";
			gallowsSprites[i] = new Sprite(program, name);
		}
	}
}
