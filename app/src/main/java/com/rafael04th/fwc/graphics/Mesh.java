package com.rafael04th.fwc.graphics;

import java.nio.Buffer;
import static android.opengl.GLES20.*;

public class Mesh {
  private final int vbo;
  private final int ibo;
  private final VertexFormat format;
  private int count;
  
  public Mesh(Buffer vertices, int vbytes, Buffer indices, int ibytes, int count, VertexFormat format) {
    this.vbo = makeBuffer(GL_ARRAY_BUFFER, vertices, vbytes);
    this.ibo = makeBuffer(GL_ELEMENT_ARRAY_BUFFER, indices, ibytes);
    this.format = format;
    this.count = count;
  }
  
  public Mesh(MeshData data, VertexFormat fmt) {
    this(data.getVertices(), data.getVertexBytes(), data.getIndices(), data.getIndexBytes(), data.getIndexCount(), fmt);
  }
  
  public void bind() {
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
  }
  
  public void unbind() {
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
  }
  
  /*
    Maybe should be in a renderer?
  */
  public void render(int shape) {
    bind();
    glDrawElements(shape, count, GL_UNSIGNED_INT, 0);
  }
  
  public void render() {
    render(GL_TRIANGLES);
  }
  
  public VertexFormat getFormat() {
    return format;
  }
  
  public int getCount() {
    return count;
  }
  
  private int makeBuffer(int tp, Buffer bf, int bs) {
    int[] ids = new int[1];
    glGenBuffers(1, ids, 0);
    glBindBuffer(tp, ids[0]);
    glBufferData(tp, bs, bf, GL_STATIC_DRAW);
    return ids[0];
  }
  
  public void dispose() {
    glDeleteBuffers(2, new int[]{vbo, ibo}, 0);
  }

  public void reuploadData(MeshData data) {
    bind();
    glBufferData(GL_ARRAY_BUFFER, data.getVertexBytes(), data.getVertices(), GL_DYNAMIC_DRAW);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.getIndexBytes(), data.getIndices(), GL_DYNAMIC_DRAW);
    count = data.getIndexCount();
  }
}
