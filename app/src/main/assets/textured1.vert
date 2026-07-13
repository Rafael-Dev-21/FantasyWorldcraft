attribute vec3 aPosition;
attribute vec2 aTexCoord;
uniform mat4 uMVP;
varying vec2 texCoord;
void main()
{
  gl_Position = uMVP * vec4(aPosition, 1);
  texCoord = aTexCoord;
}