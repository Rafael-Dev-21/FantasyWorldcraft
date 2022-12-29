package com.rboladao.rgine;

import android.opengl.GLES30;
import java.nio.IntBuffer;

public class IndexBuffer {
  private int rendererID;
  private int count;

  public IndexBuffer(final IntBuffer data, int count) {
    this.count = count;

    int[] temp = new int[1];
    GLES30.glGenBuffers(1, temp, 0);
    rendererID = temp[0];
    GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, rendererID);
    GLES30.glBufferData(
        GLES30.GL_ELEMENT_ARRAY_BUFFER, count * Integer.BYTES, data, GLES30.GL_STATIC_DRAW);
  }

  public int size() {
    return count;
  }

  public void bind() {
    GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, rendererID);
  }

  public void unbind() {
    GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
  }

  public void finalize() {
    GLES30.glDeleteBuffers(1, new int[] {rendererID}, 0);
  }
}
