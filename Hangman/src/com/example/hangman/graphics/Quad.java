package com.example.hangman.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;

public class Quad
{
	private FloatBuffer vertexBuffer, texCoordsBuffer;
    private int positionHandle, texCoordHandle;

    static final int vertCoordsSize = 3;
    static float vertCoords[] =
    {
        -1f, -1f, 0f,		// bottom left
         1f, -1f, 0f,		// bottom right
        -1f,  1f, 0f,		// top left
         1f,  1f, 0f		// top right
    };
    
    static final int texCoordsSize = 2;
    static float texCoords[] = {
    	0f, 1f,		// bottom left
    	1f, 1f,		// bottom right
    	0f, 0f,		// top left
    	1f, 0f		// top right
    };

    public Quad(int program)
    {
    	positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
    	texCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoords");
    	
        ByteBuffer bb = ByteBuffer.allocateDirect(vertCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertCoords);
        vertexBuffer.position(0);
        
        bb = ByteBuffer.allocateDirect(texCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        texCoordsBuffer = bb.asFloatBuffer();
        texCoordsBuffer.put(texCoords);
        texCoordsBuffer.position(0);
    }
    
    public void draw()
    {
    	vertexBuffer.position(0);
    	GLES20.glEnableVertexAttribArray(positionHandle);
    	GLES20.glVertexAttribPointer(positionHandle, vertCoordsSize, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    	texCoordsBuffer.position(0);
    	GLES20.glEnableVertexAttribArray(texCoordHandle);
    	GLES20.glVertexAttribPointer(texCoordHandle, texCoordsSize, GLES20.GL_FLOAT, false, 0, texCoordsBuffer);
    	
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertCoords.length);
    	
    	GLES20.glDisableVertexAttribArray(positionHandle);
    	GLES20.glDisableVertexAttribArray(texCoordHandle);
    }
}
