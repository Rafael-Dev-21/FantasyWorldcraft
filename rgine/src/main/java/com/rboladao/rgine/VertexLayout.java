package com.rboladao.rgine;

import android.opengl.GLES30;
import java.util.ArrayList;
import java.util.List;

public class VertexLayout {

  public static class Element {
    public int type;
    public int count;
    public boolean normalized;

    public Element(int type, int count, boolean normalized) {
      this.type = type;
      this.count = count;
      this.normalized = normalized;
    }
  }

  private List<Element> elements = new ArrayList<>();
  private int stride;

  public void push(int type, int count, boolean normalized) {
    elements.add(new Element(type, count, normalized));
    stride = stride + count * sizeOfType(type);
  }

  public int getStride() {
    return stride;
  }

  public List<Element> getElements() {
    return elements;
  }

  public static int sizeOfType(int type) {
    switch (type) {
      case GLES30.GL_FLOAT:
        return 4;
      case GLES30.GL_UNSIGNED_INT:
        return 4;
      case GLES30.GL_UNSIGNED_BYTE:
        return 1;
    }
    return 0;
  }
}
