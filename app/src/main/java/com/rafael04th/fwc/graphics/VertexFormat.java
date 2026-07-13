package com.rafael04th.fwc.graphics;

import java.util.ArrayList;
import java.util.List;
import static android.opengl.GLES20.*;

public class VertexFormat {
  public static class Element {
    public final int type;
    public final int count;
    public final boolean normalized;
    public final String name;
    
    public Element(int t, int c, boolean nrm, String nm) {
      type = t;
      count = c;
      normalized = nrm;
      name = nm;
    }
  }
  
  private List<Element> elements = new ArrayList<>();
  private int stride = 0;
  
  public void push(int type, int count, boolean normalized, String name) {
    elements.add(new Element(type, count, normalized, name));
    stride += count * sizeForType(type);
  }
  
  public List<Element> getElements() {
    return elements;
  }
  
  public int getStride() {
    return stride;
  }
  
  public static int sizeForType(int type) {
    switch (type) {
      case GL_FLOAT:
        return Float.BYTES;
      case GL_UNSIGNED_INT:
        return Integer.BYTES;
      case GL_UNSIGNED_BYTE:
        return Byte.BYTES;
    }
    return 0;
  }
}
