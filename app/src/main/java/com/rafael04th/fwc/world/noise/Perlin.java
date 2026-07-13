package com.rafael04th.fwc.world.noise;

import com.rafael04th.fwc.util.SplitMix32;

public class Perlin extends GradientNoise {
  public Perlin(SplitMix32 mix) {
    super(mix);
  }

  public float get(float x, float y) {
    int ix = (int)Math.floor(x);
    int iy = (int)Math.floor(y);
    float fx = x-ix;
    float fy = y-iy;
    float u = fade(fx);
    float v = fade(fy);

    int
      h00 = mix.hash(ix, iy),
      h01 = mix.hash(ix+1, iy),
      h10 = mix.hash(ix, iy+1),
      h11 = mix.hash(ix+1, iy+1);
    
    float
      g00 = dotGrad(h00, fx, fy),
      g01 = dotGrad(h01, fx-1, fy),
      g10 = dotGrad(h10, fx, fy-1),
      g11 = dotGrad(h11, fx-1, fy-1);
    
    return
      lerp(
        lerp(g00, g01, u),
        lerp(g10, g11, u), v);
  }
}
