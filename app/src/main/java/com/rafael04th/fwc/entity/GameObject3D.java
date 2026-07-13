package com.rafael04th.fwc.entity;

import com.rafael04th.fwc.world.AABB;
import org.joml.Vector3f;

public class GameObject3D {
  public final Vector3f position;
  public final AABB bounds;
  protected final AABB translatedBounds = new AABB(new Vector3f(), new Vector3f());

  public GameObject3D(
      float x,
      float y,
      float z,
      float minX,
      float minY,
      float minZ,
      float maxX,
      float maxY,
      float maxZ) {
    position = new Vector3f(x, y, z);
    bounds = new AABB(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));
  }

  public AABB getWorldBounds() {
    bounds.min.add(position, translatedBounds.min);
    bounds.max.add(position, translatedBounds.max);
    return translatedBounds;
  }
}
