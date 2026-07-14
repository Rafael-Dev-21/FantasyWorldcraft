package com.rafael04th.fwc.graphics;

import java.util.ArrayList;
import java.util.List;

public class MeshBuilder {
  private static final float[][] DA = new float[][]{
    {0, -1f, 0}, {0, -1f, 0},
    {0, -1f, 0}, {0, -1f, 0},
    {0, 0, 1f}, {0, 0, -1f}
  };
  private static final float[][] RA = new float[][]{
    {1f, 0, 0}, {-1f, 0, 0},
    {0, 0, 1f}, {0, 0, -1f},
    {1f, 0, 0}, {1f, 0, 0}
  };
  private static final float[][] CA = new float[][] {
    {0.7f, 0.7f, 0.7f}, {0.7f, 0.7f, 0.7f},
    {0.5f, 0.5f, 0.5f}, {0.5f, 0.5f, 0.5f},
    {1.0f, 1.0f, 1.0f}, {0.3f, 0.3f, 0.3f}
  };
  private static final float[][] CC = new float[][] {
    {-0.5f, 0.5f, 0.5f}, {0.5f, 0.5f, -0.5f},
    {-0.5f, 0.5f, -0.5f}, {0.5f, 0.5f, 0.5f},
    {-0.5f, 0.5f, -0.5f}, {-0.5f, -0.5f, 0.5f}
  };
  
  private final List<Float> vertices = new ArrayList<>();
  private final List<Integer> indices = new ArrayList<>();
  
  private final boolean hasNormal, hasUv, hasColor;
  
  private int vertexCount = 0;
  private final int vertexStride;
  private final int atlasCols, atlasRows;
  
  private final float tileWidth, tileHeight;
  
  public MeshBuilder(boolean n, boolean u, boolean c, int ac, int ar) {
    hasNormal = n;
    hasUv = u;
    hasColor = c;
    atlasCols = ac;
    atlasRows = ar;
    tileWidth = 1f / ac;
    tileHeight = 1f / ar;
    
    vertexStride = 3
      + (hasNormal ? 3 : 0)
      + (hasUv ? 2 : 0)
      + (hasColor ? 3 : 0);
  }
  
  public MeshBuilder vertex(float x, float y, float z, float nx, float ny, float nz, float u, float v, float r, float g, float b) {
    pos(x, y, z)
      .norm(nx, ny, nz)
      .uv(u, v)
      .color(r, g, b);
    vertexCount++;
    return this;
  }
  
  public MeshBuilder quad(float[] pos, float[] down, float[] right, float[] color, int tile) {
    float[] norm = new float[] {
      down[1] * right[2] - down[2] * right[1],
      down[2] * right[0] - down[0] * right[2],
      down[0] * right[1] - down[1] * right[0]
    };
    
    float len = (float)Math.sqrt(norm[0]*norm[0]+norm[1]*norm[1]+norm[2]*norm[2]);
    if (len > 0.0001f) {
      norm[0] /= len;
      norm[1] /= len;
      norm[2] /= len;
    }
    
    int baseVertex = vertexCount;
    
    float u = (tile % atlasCols) / (float)atlasCols;
    float v = (tile / atlasCols) / (float)atlasRows;
    
    return
      vertex(pos[0], pos[1], pos[2], norm[0], norm[1], norm[2], u, v, color[0], color[1], color[2])
        .vertex(pos[0]+down[0], pos[1]+down[1], pos[2]+down[2], norm[0], norm[1], norm[2], u, v+tileHeight, color[0], color[1], color[2])
        .vertex(pos[0]+down[0]+right[0], pos[1]+down[1]+right[1], pos[2]+down[2]+right[2], norm[0], norm[1], norm[2], u+tileWidth, v+tileHeight, color[0], color[1], color[2])
        .vertex(pos[0]+right[0], pos[1]+right[1], pos[2]+right[2], norm[0], norm[1], norm[2], u+tileWidth, v, color[0], color[1], color[2])
        .indices(baseVertex, baseVertex+1, baseVertex+2, baseVertex, baseVertex+2, baseVertex+3);
  }
  
  public MeshBuilder face(float[] pos, int face, int tile) {
    assert (face >= 0);
    assert (face < 6);
    
    return quad(new float[]{pos[0]+CC[face][0], pos[1]+CC[face][1], pos[2]+CC[face][2]}, DA[face], RA[face], CA[face], tile);
  }
  
  public MeshBuilder cube(float[] pos, int[] tile) {
    for (int i = 0; i < 6; i++) {
      face(pos, i, i == 5 ? tile[2] : (i == 4 ? tile[0] : tile[1]));
    }
    return this;
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
  
  private MeshBuilder pos(float x, float y, float z) {
    vertexData(x, y, z);
    return this;
  }
  
  private MeshBuilder norm(float nx, float ny, float nz) {
    if (hasNormal)
      vertexData(nx, ny, nz);
    return this;
  }
  
  private MeshBuilder uv(float u, float v) {
    if (hasUv)
      vertexData(u, v);
    return this;
  }
  
  private MeshBuilder color(float r, float g, float b) {
    if (hasColor)
      vertexData(r, g, b);
    return this;
  }
  
  private MeshBuilder vertexData(float... fs) {
    for (float f : fs) {
      vertices.add(f);
    }
    return this;
  }
  
  public MeshBuilder indices(int... is) {
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
