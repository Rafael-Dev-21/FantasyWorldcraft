package com.rafael04th.fwc.world.meshing;

import com.rafael04th.fwc.graphics.Mesh;
import com.rafael04th.fwc.graphics.VoxelFormats;
import com.rafael04th.fwc.world.Chunk;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ChunkUploader {
  private final BlockingQueue<Chunk> pending = new LinkedBlockingQueue<>();

  public void put(Chunk c) throws InterruptedException {
    if (c.isMeshed())
      pending.put(c);
  }

  public void update() {
    int count = 0;
    Chunk c = null;
    while ((c = pending.poll()) != null && count < 16) {
      count++;
      if (c.getPendingMesh() != null) {
        Mesh newMesh = new Mesh(c.getPendingMesh(), VoxelFormats.POSITION_UV_COLOR);
        c.setMesh(newMesh);
        c.consumePendingMesh();
      }
    }
  }
}
