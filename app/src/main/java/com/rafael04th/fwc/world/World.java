package com.rafael04th.fwc.world;

import com.rafael04th.fwc.entity.Player;
import com.rafael04th.fwc.graphics.Camera;
import com.rafael04th.fwc.world.gen.ChunkMaker;
import com.rafael04th.fwc.world.meshing.ChunkMesher;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.joml.Vector3f;

public class World {
  private Map<Long, Chunk> chunks = new ConcurrentHashMap<>();
  private int wi, wj;
  private ChunkMaker cm;

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

  // TODO!: maybe a Ray?
  public Optional<BlockHit> hit(Camera cam, float maxdist) {
    Vector3f O = cam.getPosition();
    Vector3f D = cam.getDirection();
    float sideDistX, sideDistY, sideDistZ;
    float deltaDistX = Math.abs(D.x) < 0.001f ? 1e30f : Math.abs(1f / D.x);
    float deltaDistY = Math.abs(D.y) < 0.001f ? 1e30f : Math.abs(1f / D.y);
    float deltaDistZ = Math.abs(D.z) < 0.001f ? 1e30f : Math.abs(1f / D.z);
    int mapX = (int)O.x;
    int mapY = (int)O.y;
    int mapZ = (int)O.z;
    int stepX, stepY, stepZ;
    float dist;
    
    if (D.x < 0) {
      stepX = -1;
      sideDistX = (O.x - mapX) * deltaDistX;
    } else {
      stepX = 1;
      sideDistX = (mapX + 1.0f - O.x) * deltaDistX;
    }
    
    if (D.y < 0) {
      stepY = -1;
      sideDistY = (O.y - mapY) * deltaDistY;
    } else {
      stepY = 1;
      sideDistY = (mapY + 1.0f - O.y) * deltaDistY;
    }
    
    if (D.z < 0) {
      stepZ = -1;
      sideDistZ = (O.z - mapZ) * deltaDistZ;
    } else {
      stepZ = 1;
      sideDistZ = (mapZ + 1.0f - O.z) * deltaDistZ;
    }
    
    int axis = 0;
    boolean hit = false;
    dist = 0;
    
    int accX = 0, accY = 0, accZ = 0;
    Block b = null;
    while (!hit && dist < maxdist*maxdist) {
      if (sideDistX < sideDistY && sideDistX < sideDistZ) {
        sideDistX += deltaDistX;
        mapX += stepX;
        accX += stepX;
        axis = 0;
      } else if (sideDistY < sideDistZ) {
        sideDistY += deltaDistY;
        mapY += stepY;
        accY += stepY;
        axis = 1;
      } else {
        sideDistZ += deltaDistZ;
        mapZ += stepZ;
        accZ += stepZ;
        axis = 2;
      }
      dist = accX * accX + accY * accY + accZ * accZ;
      Chunk c = atF(mapX, mapZ);
      int cx = mapX % Chunk.WIDTH;
      if (cx < 0) cx += Chunk.WIDTH;
      int cy = mapY % Chunk.HEIGHT;
      if (cy < 0) cy += Chunk.HEIGHT;
      int cz = mapZ % Chunk.DEPTH;
      if (cz < 0) cz += Chunk.DEPTH;
      if (c == null) {
        return Optional.empty();
      }
      if ((b = c.get(cx, cy, cz)) != Blocks.AIR) hit = true;
    }
    if (dist >= maxdist * maxdist) {
      return Optional.empty();
    }
    dist = (float)Math.sqrt(dist);
    
    float normalX=0, normalY=0, normalZ=0;
    switch (axis) {
      case 0:
        normalX = -stepX;
        break;
      case 1:
        normalY = -stepY;
        break;
      case 2:
        normalZ = -stepZ;
        break;
    }
    
    return Optional.of(new BlockHit(mapX, mapY, mapZ, normalX, normalY, normalZ, dist, b));
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
