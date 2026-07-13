package com.rafael04th.fwc.world.noise;

import com.rafael04th.fwc.util.SplitMix32;

public abstract class GradientNoise implements Noise {
  protected static final float[][] GRADS = new float[][] {
    {0, 1},
    {1, 1},
    {1, 0},
    {1, -1},
    {0, -1},
    {-1, -1},
    {-1, 0},
    {-1, 1}
  };

  protected SplitMix32 mix;

  public GradientNoise(SplitMix32 mix) {
    this.mix = mix;
  }

  protected float dotGrad(int i, float x, float y) {
    return GRADS[i&7][0] * x + GRADS[i&7][1] * y;
  }
  
  protected float lerp(float a, float b, float t) {
    return (1-t)*a+t*b;
  }
  
  protected float fade(float t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }
}
