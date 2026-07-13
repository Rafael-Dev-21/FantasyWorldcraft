precision lowp float;

uniform sampler2D uTex;
varying vec2 texCoord;
varying vec3 meshColor;
void main()
{
  gl_FragColor = texture2D(uTex, texCoord) * vec4(meshColor, 1);
}
