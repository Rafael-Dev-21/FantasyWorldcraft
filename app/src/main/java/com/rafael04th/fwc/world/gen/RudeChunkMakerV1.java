package com.rafael04th.fwc.world.gen;

import com.rafael04th.fwc.util.SplitMix32;
import com.rafael04th.fwc.world.Block;
import com.rafael04th.fwc.world.Blocks;
import com.rafael04th.fwc.world.Chunk;
import com.rafael04th.fwc.world.noise.Fbm;
import com.rafael04th.fwc.world.noise.Perlin;


public class RudeChunkMakerV1 implements ChunkMaker {
  private long seed;
  private Fbm fbm, cont, eros, peak;
  private float playerY;
  public RudeChunkMakerV1(long seed) {
    this.seed = seed;
    SplitMix32 mix = new SplitMix32(seed);
    Perlin perlin = new Perlin(mix);
    fbm = new Fbm(perlin);
    cont = new Fbm(perlin);
    eros = new Fbm(perlin);
    peak = new Fbm(perlin);
    
    cont.amplitude(2);
    cont.frequency(2);
    eros.amplitude(0.5f);
    eros.frequency(0.5f);
    peak.amplitude(5);
    peak.frequency(4);
    peak.octaves(7);
    
    fbm.octaves(1);
  }

  public Chunk newChunk(int x, int z) {
    final int SANDLIMIT = 23;
    final int SNOWLIMIT = 35;
    Chunk c = new Chunk(x, z);
    for (int bx = 0; bx < Chunk.WIDTH; bx++) {
      for (int bz = 0; bz < Chunk.DEPTH; bz++) {
        float e = fbm.get(bx/(float)Chunk.WIDTH+x-0.5f, bz/(float)Chunk.DEPTH+z-0.5f);
        //e = e * 0.5f + 0.5f;
        int height = getHeight(e);
        int rockStart = height - 3;
        int dirtStart = height - 1;
        for (int i = 0; i < height; i++) {
          if (i > rockStart) {
            if (i < SANDLIMIT) {
              c.set(bx, i, bz, Blocks.SAND);
            } else {
              if (i < dirtStart) {
                c.set(bx, i, bz, Blocks.DIRT);
              } else {
                Block cover = i < SNOWLIMIT
                  ? Blocks.GRASS
                  : Blocks.SNOW;
                c.set(bx, i, bz, cover);
              }
            }
          } else {
            c.set(bx, i, bz, Blocks.ROCK);
          }
        }
      }
    }
    return c;
  }

  public float playerStartX() {
    return 0;
  }

  public float playerStartY() {
    float e = fbm.get(-0.5f, -0.5f);
    //e = e * 0.5f + 0.5f;
    int height = getHeight(e);
    return height + 3;
  }

  public float playerStartZ() {
    return 0;
  }
  
  private int clamp(int v, int mn, int mx) {
    return Math.max(mn, Math.min(mx, v));
  }
  
  private int getHeight(float e) {
    return (int)clamp((int)(e * 8)+32, 1, Chunk.HEIGHT);
  }
  
  private float cont(float cx, float cz, float x, float z) {
    return cont.get(x/Chunk.WIDTH+cx-0.5f, z/Chunk.DEPTH+cz-0.5f);
  }

  private float eros(float cx, float cz, float x, float z) {
    return eros.get(x/Chunk.WIDTH+cx-0.5f, z/Chunk.DEPTH+cz-0.5f);
  }
  
  private float peak(float cx, float cz, float x, float z) {
    return peak.get(x/Chunk.WIDTH+cx-0.5f, z/Chunk.DEPTH+cz-0.5f);
  }
}
