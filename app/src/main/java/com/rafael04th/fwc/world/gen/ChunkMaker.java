package com.rafael04th.fwc.world.gen;

import com.rafael04th.fwc.world.Chunk;

public interface ChunkMaker {
  Chunk newChunk(int x, int z);
  
  float playerStartX();
  float playerStartY();
  float playerStartZ();
}
