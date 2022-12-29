precision mediump float;

uniform sampler2D Sample0;

varying vec2 TexCoord;

void main() {
  gl_FragColor = texture2D(Sample0, TexCoord);
}