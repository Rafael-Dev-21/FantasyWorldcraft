package com.rafael04th.fwc.world.noise;

public class Fbm implements Noise {
  
  private float frequency=1;
  private float amplitude=1;
  private float lacunarity=2;
  private float persistence=0.5f;
  private int octaves=3;
  
  private final Noise base;
  
  public Fbm(Noise base) {
    this.base = base;
  }

  public void frequency(float f) {
    frequency = Math.max(0.001f, f);
  }
  public void amplitude(float f) {
    amplitude = Math.max(0.001f, f);
  }
  public void lacunarity(float f) {
    lacunarity = Math.max(0.001f, f);
  }
  public void persistence(float f) {
    persistence = Math.max(0.001f, f);
  }
  public void octaves(int i) {
    octaves = Math.max(1, i);
  }
  public float get(float x, float y) {
    float xa = x * frequency;
    float ya = y * frequency;
    float amp = amplitude;
    float div = 0.0f;
    float res = 0.0f;
    
    for (int o = 0; o < octaves; o++) {
      res += base.get(xa, ya) * amp;
      div += amp;
      xa *= lacunarity;
      ya *= lacunarity;
      amp *= persistence;
    }
    return res / div;
  }
}
