#version 300 es

layout(location = 0) in vec3 Position;
// attribute vec4 VertexColor;

// uniform mat4 MVPMatrix;

out vec4 Color;

void main() {
  gl_Position.xyz = Position;
  gl_Position.w = 1.0;
  Color = vec4(1.0);
}