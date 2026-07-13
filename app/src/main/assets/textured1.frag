precision lowp float;

uniform sampler2D uTex;
varying vec2 texCoord;
void main()
{
  gl_FragColor = texture2D(uTex, texCoord);
}