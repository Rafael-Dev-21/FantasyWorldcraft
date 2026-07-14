package com.rafael04th.fwc.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MeshData {

  private final FloatBuffer vertices;
  private final IntBuffer indices;
  private final int vertexCount;
  private final int indexCount;
  private final int vertexSize;

  public MeshData(FloatBuffer vert, int vc, IntBuffer ind, int ic, int sz) {
    vertices = vert;
    vertexCount = vc;
    indices = ind;
    indexCount = ic;
    vertexSize = sz;
  }

  public MeshData(
      int vertSiz, float[] vert, int vStart, int vSize, int[] ind, int iStart, int iSize) {
    assert (vertSiz > 0);
    assert (vert != null);
    assert (ind != null);
    assert (vStart >= 0);
    assert (vSize > 0);
    assert (vStart + vSize <= vert.length);
    assert (iStart >= 0);
    assert (iSize > 0);
    assert (iStart + iSize <= ind.length);

    FloatBuffer fb =
        ByteBuffer.allocateDirect(vSize * Float.BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
    fb.put(vert, vStart, vSize);
    fb.position(0);

    IntBuffer sb =
        ByteBuffer.allocateDirect(iSize * Integer.BYTES)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
    sb.put(ind, iStart, iSize);
    sb.position(0);

    vertices = fb;
    vertexCount = vSize / vertSiz;
    indices = sb;
    indexCount = iSize;
    vertexSize = vertSiz;
  }

  public FloatBuffer getVertices() {
    return vertices;
  }

  public IntBuffer getIndices() {
    return indices;
  }

  public int getVertexCount() {
    return vertexCount;
  }

  public int getIndexCount() {
    return indexCount;
  }

  public int getVertexSize() {
    return vertexSize;
  }
  
  public int getVertexBytes() {
    return vertexSize * vertexCount * Float.BYTES;
  }
  
  public int getIndexBytes() {
    return indexCount * Integer.BYTES;
  }
}
