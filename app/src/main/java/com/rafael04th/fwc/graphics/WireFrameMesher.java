package com.rafael04th.fwc.graphics;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


public class WireFrameMesher {

  private static final float[][] CC = new float[][] {
    {0.5f, 0.5f, 0.5f},
    {0.5f, 0.5f, -0.5f}
  };
  
  public static final float[][] DA = new float[][] {
    {0, -1, 0}, {0, -1, 0}
  };
  
  public static final float[][] RA = new float[][] {
    {-1, 0, 0}, {-1, 0, 0}
  };

private List<Float> vertices = new ArrayList<>();
  private List<Integer> indices = new ArrayList<>();
  private int vertexCount = 0;
  private int vertexStride;

  public WireFrameMesher() {
    vertexStride = 3;
  }
  
  public WireFrameMesher vertex(float x, float y, float z) {
    pos(x, y, z);
    vertexCount++;
    return this;
  }
  
  public WireFrameMesher quad(float[] pos, float[] down, float[] right) {
    int baseVertex = vertexCount;
    return
      vertex(pos[0], pos[1], pos[2])
        .vertex(pos[0]+down[0], pos[1]+down[1], pos[2]+down[2])
        .vertex(pos[0]+down[0]+right[0], pos[1]+down[1]+right[1], pos[2]+down[2]+right[2])
        .vertex(pos[0]+right[0], pos[1]+right[1], pos[2]+right[2])
        .indices(baseVertex, baseVertex+1, baseVertex+1, baseVertex+2, baseVertex+2, baseVertex+3, baseVertex+3, baseVertex);
  }
  
  public WireFrameMesher cube(float[] pos) {
    int baseVertex = vertexCount;
    return
      quad(new float[]{pos[0]+CC[0][0],pos[1]+CC[0][1],pos[2]+CC[0][2]}, DA[0], RA[0])
        .quad(new float[]{pos[0]+CC[1][0],pos[1]+CC[1][1],pos[2]+CC[1][2]}, DA[1], RA[1])
        .indices(
          baseVertex, baseVertex+4,
          baseVertex+1, baseVertex+5,
          baseVertex+2, baseVertex+6,
          baseVertex+3, baseVertex+7
        );
  }
  
  public MeshData build() {
    float[] vert = new float[vertices.size()];
    for (int i = 0; i < vertices.size(); i++)
      vert[i] = vertices.get(i);
    int[] ind = new int[indices.size()];
    for (int i = 0; i < indices.size(); i++)
      ind[i] = indices.get(i);
 
    return new MeshData(vertexStride, vert, 0, vert.length, ind, 0, ind.length);
  }
  
  private WireFrameMesher pos(float x, float y, float z) {
    vertexData(x, y, z);
    return this;
  }
  
  private WireFrameMesher vertexData(float... fs) {
    for (float f : fs) {
      vertices.add(f);
    }
    return this;
  }
  
  public WireFrameMesher indices(int... is) {
    for (int i : is) {
      indices.add(i);
    }
    return this;
  }
  
  public void clear() {
    clear(true, true);
  }
  
  public void clear(boolean i, boolean v) {
    if (i) {
      indices.clear();
    }
    if (v) {
      vertices.clear();
      vertexCount = 0;
    }
  }
}
