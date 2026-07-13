package com.rafael04th.fwc.physics;

import com.rafael04th.fwc.world.AABB;
import org.joml.Vector3f;

public interface KinematicBody {
  Vector3f getPosition();
  Vector3f getVelocity();
  AABB getBounds();
  boolean isGrounded();
  void setGrounded(boolean grounded);
  float getMass();
}
