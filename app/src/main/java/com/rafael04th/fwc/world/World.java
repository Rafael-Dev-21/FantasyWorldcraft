package com.rafael04th.fwc.world;

import com.rafael04th.fwc.entity.Player;
import com.rafael04th.fwc.world.gen.ChunkMaker;
import com.rafael04th.fwc.world.meshing.ChunkMesher;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class World {
  private final Map<Long, Chunk> chunks = new ConcurrentHashMap<>();
  private int wi, wj;
  private final ChunkMaker cm;

  public World(ChunkMaker cm, float x, float z) {
    this.cm = cm;
    wi = (int) (x / Chunk.WIDTH);
    wj = (int) (z / Chunk.DEPTH);
  }

  public void update(float x, float z, ChunkMesher m) {
    int newWi = (int) (Math.floor(x / Chunk.WIDTH));
    int newWj = (int) (Math.floor(z / Chunk.DEPTH));
    wi = newWi;
    wj = newWj;

    for (int i = -3; i < 4; i++) {
      for (int j = -3; j < 4; j++) {
        long id = ((long) (j + wj) << 32L) | ((i + wi) & 0xFFFFFFFFL);
        Chunk c = null;
        if (!chunks.containsKey(id)) {
          c = cm.newChunk(i+wi, j+wj) /*new Chunk(i + wi, j + wj)*/;
          chunks.put(id, c);
        } else {
          c = at(i + wi, j + wj);
        }
        if (c != null && c.trySetMeshing()) {
          m.put(c);
        }
      }
    }
  }
  
  public Player newPlayer() {
    Player p = new Player();
    p.getPosition().x = cm.playerStartX();
    p.getPosition().y = cm.playerStartY();
    p.getPosition().z = cm.playerStartZ();
    return p;
  }

  public Chunk at(int i, int j) {

    int wx = i;
    int wz = j;
    long id = ((long) wz << 32L) | (wx & 0xFFFFFFFFL);
    if (!chunks.containsKey(id)) return null;
    return chunks.get(id);
  }



  public Chunk atF(float x, float z) {
    return at((int) (Math.floor(x / Chunk.WIDTH)), (int) (Math.floor(z / Chunk.DEPTH)));
  }

  public synchronized Optional<Block> get(int x, int y, int z) {
    Chunk c = atF(x, z);
    if (c == null) {
      return Optional.empty();
    }
    int cx = x % Chunk.WIDTH;
    if (cx < 0) cx += Chunk.WIDTH;
    int cy = y % Chunk.HEIGHT;
    if (cy < 0) cy += Chunk.HEIGHT;
    int cz = z % Chunk.DEPTH;
    if (cz < 0) cz += Chunk.DEPTH;
    return Optional.of(c.get(cx, cy, cz));
  }
  
  public synchronized void set(int x, int y, int z, Block b) {
    Chunk c = atF(x, z);
    if (c == null) {
      return;
    }
    int cx = x % Chunk.WIDTH;
    if (cx < 0) cx += Chunk.WIDTH;
    int cy = y % Chunk.HEIGHT;
    if (cy < 0) cy += Chunk.HEIGHT;
    int cz = z % Chunk.DEPTH;
    if (cz < 0) cz += Chunk.DEPTH;
    c.set(cx, cy, cz, b);
    if (cx == 0 && at(c.x()-1, c.z()) != null)
      at(c.x()-1, c.z()).setDirty();
    if (cz == 0 && at(c.x(), c.z()-1) != null)
      at(c.x(), c.z()-1).setDirty();
    if (cx == Chunk.WIDTH-1 && at(c.x()+1, c.z()) != null)
      at(c.x()+1, c.z()).setDirty();
    if (cz == Chunk.DEPTH-1 && at(c.x(), c.z()+1) != null)
      at(c.x(), c.z()+1).setDirty();
  }

  public void dispose() {

    for (Chunk c : chunks.values()) if (c != null) c.dispose();
  }
}
