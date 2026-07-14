package com.rafael04th.fwc.world.meshing;

import android.util.Log;

import com.rafael04th.fwc.graphics.MeshBuilder;
import com.rafael04th.fwc.graphics.MeshData;
import com.rafael04th.fwc.world.Block;
import com.rafael04th.fwc.world.Blocks;
import com.rafael04th.fwc.world.Chunk;
import com.rafael04th.fwc.world.World;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChunkMesher {
  private class WorkerMesher implements Runnable {
    final MeshBuilder builder = new MeshBuilder(hasNormal, hasUv, hasColor, atlasCols, atlasRows);
  
    public void run() {
      while (running) {
        try {
          Chunk c = queue.poll(16, TimeUnit.MILLISECONDS);
          if (c == null)
            continue;
          builder.clear();
          c.setPendingMesh(mesh(builder, w, c));
          c.setMeshed();
          up.put(c);
        } catch (InterruptedException e) {
          Log.e("ChunkMesher.class", "Could not mesh chunk", e);
        }
      }
    }
  }

  private static final int[][] NEIGHBORS = new int[][] {
    {0, 0, 1}, {0, 0, -1},
    {-1, 0, 0}, {1, 0, 0},
    {0, 1, 0}, {0, -1, 0}
  };

  private final boolean hasNormal, hasUv, hasColor;
  private final int atlasCols, atlasRows;
  private volatile boolean running = false;
  
  private final BlockingQueue<Chunk> queue = new ArrayBlockingQueue<>(256);
  
  private final World w;
  private final ChunkUploader up;

  public ChunkMesher(World w, ChunkUploader u, boolean n, boolean t, boolean c, int ac, int ar) {
    this.w = w;
    this.up = u;
    hasNormal = n;
    hasUv = t;
    hasColor = c;
    atlasCols = ac;
    atlasRows = ar;
  }
  
  public void start() {
    running = true;
    int count = Runtime.getRuntime().availableProcessors()-1;
    if (count == 0) count = 1;
    for (int i = 0; i < count; i++)
      new Thread(new WorkerMesher(), "FWC Mesher " + i).start();
  }
  
  public void stop() {
    running = false;
  }
  
  public void put(Chunk c) {
    try {
      queue.put(c);
    } catch (InterruptedException e) {
      Log.e("ChunkMesher.class", "Could not put chunk", e);
    }
  }

  private MeshData mesh(MeshBuilder builder, World world, Chunk chunk) {
    for (int i = 0; i < Chunk.WIDTH; i++) {
      for (int j = 0; j < Chunk.HEIGHT; j++) {
        for (int k = 0; k < Chunk.DEPTH; k++) {
          if (chunk.get(i,j,k) != Blocks.AIR)
            mesh(builder, world, chunk, i, j, k, chunk.get(i, j, k));
        }
      }
    }
    return builder.build();
  }
  
  private void mesh(MeshBuilder builder, World world, Chunk chunk, int i, int j, int k, Block block) {
    for (int face = 0; face < 6; face++) {
      int[] neighbor = NEIGHBORS[face];
      int ni = neighbor[0] + i;
      int nj = neighbor[1] + j;
      int nk = neighbor[2] + k;
      if (ni < 0 || ni >= Chunk.WIDTH || nj < 0 || nj >= Chunk.HEIGHT || nk < 0 || nk >= Chunk.DEPTH) {
        if (face >= 4) {
          builder.face(new float[]{i, j, k}, face, face == 4 ? block.topTile : block.bottomTile);
        } else {
          int cbx = neighbor[0] + chunk.x();
          int cbz = neighbor[2] + chunk.z();
          int cbi = (ni + Chunk.WIDTH) % Chunk.WIDTH;
          int cbk = (nk + Chunk.DEPTH) % Chunk.DEPTH;
          Chunk cb = world.at(cbx, cbz);
          if (cb == null || cb.get(cbi, j, cbk) == Blocks.AIR) {
            builder.face(new float[]{i, j, k}, face, block.sideTile);
          }
        }
      } else {
        if (chunk.get(ni, nj, nk) == Blocks.AIR) {
          builder.face(new float[]{i, j, k}, face, face < 4 ? block.sideTile : (face == 4 ? block.topTile : block.bottomTile));
        }
      }
    }
  }
}
