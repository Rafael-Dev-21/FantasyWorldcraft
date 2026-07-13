attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aColor;
uniform mat4 uMVP;
varying vec2 texCoord;
varying vec3 meshColor;
void main()
{
  gl_Position = uMVP * vec4(aPosition, 1);
  texCoord = aTexCoord;
  meshColor = aColor;
}
