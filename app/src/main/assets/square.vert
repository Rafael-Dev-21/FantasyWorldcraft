attribute vec4 Position;
attribute vec2 aTexCoord;

uniform mat4 MVPMatrix;

varying vec2 TexCoord;

void main() {
  TexCoord = aTexCoord;
  gl_Position = MVPMatrix * Position;
}