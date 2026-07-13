package com.rafael04th.fwc.physics;

import org.joml.Vector3f;

public class LeapfrogIntegrator extends Integrator {
  public LeapfrogIntegrator(KinematicBody body, Mover mover) {
    super(body, mover);
  }

  public void move(Vector3f acceleration, float deltaTime) {
    Vector3f halfInc = acceleration.mul(deltaTime * 0.5f, new Vector3f());
    body.getVelocity().add(halfInc);
    mover.move(body, deltaTime);
    body.getVelocity().add(halfInc);
  }
}
