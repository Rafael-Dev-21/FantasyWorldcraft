package com.rafael04th.fwc.physics;

import org.joml.Vector3f;

public abstract class Integrator {
  public interface Mover {
    void move(KinematicBody body, float dt);
  }

  protected final KinematicBody body;
  protected final Mover mover;
  
  public Integrator(KinematicBody body, Mover mover) {
    this.body = body;
    this.mover = mover;
  }
  
  public abstract void move(Vector3f acceleration, float deltaTime);
}
