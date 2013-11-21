uniform mat4 uMVPMatrix;

attribute vec4 aPosition;
attribute vec2 aTexCoords;

varying vec2 vTexCoords;

void main()
{
	gl_Position = uMVPMatrix * aPosition;
	vTexCoords = aTexCoords;
}