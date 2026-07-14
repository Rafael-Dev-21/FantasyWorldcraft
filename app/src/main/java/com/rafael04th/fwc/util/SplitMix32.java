package com.rafael04th.fwc.util;

public class SplitMix32 {

  private final long seed;
  public SplitMix32(long seed) {
    this.seed = seed;
  }
  
  public int hash(int x, int y) {
    long h = seed;
    h = h * 31 + x;
    h = h * 31 + y;
    h += 2654435769L;
    h ^= h >> 16;
    h *= 0x85ebca6bL;
    h ^= h >> 13;
    h *= 0xc2b2ae35L;
    h ^= h >> 16;
    return (int)(h);
  }

  public float hashf(int x, int y) {
    return hash(x, y) / (float)Integer.MAX_VALUE;
  }

  public float hashradf(int x, int y) {
    return (float)(hashf(x, y) * Math.PI);
  }
}
