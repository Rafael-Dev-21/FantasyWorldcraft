package com.rafael04th.fwc.world;

import com.rafael04th.fwc.graphics.Mesh;
import com.rafael04th.fwc.graphics.MeshData;
import java.util.concurrent.atomic.AtomicInteger;
import org.joml.Vector3f;

public class Chunk {
  public static final int WIDTH = 16;
  public static final int HEIGHT = 64;
  public static final int DEPTH = 16;
  
  public static final int STATE_DIRTY = 0;
  public static final int STATE_MESHING = 1;
  public static final int STATE_MESHED = 2;

  private final Block[] blocks = new Block[WIDTH * HEIGHT * DEPTH];
  private final int x, z;
  
  private Mesh mesh = null;
  private volatile MeshData pendingMesh = null;
  private AtomicInteger state = new AtomicInteger(STATE_DIRTY);
  
  private AABB bounds;

  public Chunk(int x, int z) {
    for (int i = 0; i < WIDTH; i++) {
      for (int j = 0; j < HEIGHT; j++) {
        for (int k = 0; k < DEPTH; k++) {
          blocks[i+WIDTH*(j+k*HEIGHT)] = Blocks.AIR/*(j > 32 ? Blocks.AIR : (j == 32 && Math.random() < 0.5 ? Blocks.DIRT : Blocks.ROCK))*/;
        }
      }
    }
    this.x = x;
    this.z = z;

    bounds = new AABB(new Vector3f(x * WIDTH, 0, z * DEPTH), new Vector3f((x+1)*WIDTH, HEIGHT, (z+1) * DEPTH));
  }
  
  public boolean isDirty() {
    return state.get() == STATE_DIRTY;
  }
  
  public void setDirty() {
    state.compareAndSet(STATE_MESHED, STATE_DIRTY);
  }
  
  public boolean isMeshing() {
    return state.get() == STATE_MESHING;
  }
  
  public void setMeshing() {
    state.compareAndSet(STATE_DIRTY, STATE_MESHING);
  }
  
  public boolean trySetMeshing() {
    return state.compareAndSet(STATE_DIRTY, STATE_MESHING);
  }
  
  public boolean isMeshed() {
    return state.get() == STATE_MESHED;
  }
  
  public void setMeshed() {
    state.compareAndSet(STATE_MESHING, STATE_MESHED);
  }
  
  public synchronized Block get(int i, int j, int k) {
    return blocks[linearize(i, j, k)];
  }
  
  public synchronized void set(int i, int j, int k, Block b) {
    blocks[linearize(i, j, k)] = b;
    setDirty();
  }
  
  public int x() { return x; }
  public int z() { return z; }
  
  public synchronized void setPendingMesh(MeshData md) {
    pendingMesh = md;
  }
  
  public MeshData getPendingMesh() {
    return pendingMesh;
  }
  
  public void consumePendingMesh() {
    pendingMesh = null;
  }
  
  public void setMesh(Mesh m) {
    if (mesh != null)mesh.dispose();
    mesh = m;
  }
  
  public Mesh getMesh() {
    return mesh;
  }

  public static final int linearize(int i, int j, int k) {
    return i+WIDTH*(j+k*HEIGHT);
  }
  
  public void dispose() {
    if (mesh != null) mesh.dispose();
  }

  public AABB getBounds() {
    return bounds;
  }
}
