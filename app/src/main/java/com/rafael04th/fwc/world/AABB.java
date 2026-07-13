package com.rafael04th.fwc.world;

import org.joml.Vector3f;

public class AABB {
  public final Vector3f min, max;
  
  public AABB(Vector3f mn, Vector3f mx) {
    min = mn;
    max = mx;
  }
}
