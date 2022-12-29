package com.rboladao.rgine;

import android.opengl.GLES30;
import java.util.List;

public class VertexArray {

  private int rendererID;

  public VertexArray() {
    int[] temp = new int[1];
    GLES30.glGenVertexArrays(1, temp, 0);
    rendererID = temp[0];
  }

  public void addBuffer(VertexBuffer vb, VertexLayout layout) {
    bind();
    vb.bind();
    List<VertexLayout.Element> elements = layout.getElements();
    int offset = 0;
    for (int i = 0; i < elements.size(); i++) {
      VertexLayout.Element element = elements.get(i);
      GLES30.glEnableVertexAttribArray(i);
      GLES30.glVertexAttribPointer(
          i, element.count, element.type, element.normalized, layout.getStride(), offset);
      offset = offset + element.count * VertexLayout.sizeOfType(element.type);
    }
  }

  public void bind() {
    GLES30.glBindVertexArray(rendererID);
  }

  public void unbind() {
    GLES30.glBindVertexArray(0);
  }

  public void finalize() {
    GLES30.glDeleteVertexArrays(1, new int[] {rendererID}, 0);
  }
}
