package com.example.hangman.graphics;

import android.opengl.GLES20;

/*
 * Not safe to call from multiple threads.
 */
public class Sprite
{
	private static Quad quad = null;
	private int textureDataHandle, textureUniformHandle;
	
	public Sprite(int program, final String filename)
	{
		if(quad == null) quad = new Quad(program);
		textureDataHandle = GLRenderer.loadTextureFromAsset(filename);
		textureUniformHandle = GLES20.glGetAttribLocation(program, "uTexture");
	}
	
	public Sprite(int program, int textureDataHandle)
	{
		if(quad == null) quad = new Quad(program);
		this.textureDataHandle = textureDataHandle;
	}
	
	public void draw()
	{
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
        GLES20.glUniform1i(textureUniformHandle, 0);
        
        quad.draw();
	}
}
