package com.rafael04th.fwc.entity;

import com.rafael04th.fwc.physics.KinematicBody;
import com.rafael04th.fwc.world.AABB;
import org.joml.Vector3f;

public class DynamicGameObject3D extends GameObject3D implements KinematicBody {
  public final Vector3f velocity;
  public final Vector3f accel;
  protected boolean grounded = false;
  protected float mass = 1.0f;

  public DynamicGameObject3D(
      float x,
      float y,
      float z,
      float minX,
      float minY,
      float minZ,
      float maxX,
      float maxY,
      float maxZ) {
    super(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    velocity = new Vector3f();
    accel = new Vector3f();
  }

  /* -- KINEMATICBODY INTERFACE -- */

  @Override
  public Vector3f getPosition() {
    return position;
  }

  @Override
  public Vector3f getVelocity() {
    return velocity;
  }

  @Override
  public AABB getBounds() {
    return getWorldBounds();
  }

  @Override
  public boolean isGrounded() {
    return grounded;
  }

  @Override
  public void setGrounded(boolean grounded) {
    this.grounded = grounded;
  }

  @Override
  public float getMass() {
    return mass;
  }

  public void setMass(float mass) {
    this.mass = mass;
  }
}
