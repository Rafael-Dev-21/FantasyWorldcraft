package com.rboladao.rgine;

import android.opengl.GLES30;
import java.nio.Buffer;

public class VertexBuffer {

  private int rendererID;

  public VertexBuffer(final Buffer data, int size) {
    int[] temp = new int[1];
    GLES30.glGenBuffers(1, temp, 0);
    rendererID = temp[0];
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, rendererID);
    GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, size, data, GLES30.GL_STATIC_DRAW);
  }

  public void bind() {
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, rendererID);
  }

  public void unbind() {
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
  }
  
  public void finalize() {
    GLES30.glDeleteBuffers(1, new int[] {rendererID}, 0);
  }
}
