package com.rafael04th.fwc.physics;

import org.joml.Vector3f;


public class SemiImplicitEulerIntegrator extends Integrator {

  public SemiImplicitEulerIntegrator(KinematicBody body, Mover mover) {
    super(body, mover);
  }
  
  public void move(Vector3f acceleration, float deltaTime) {
    body.getVelocity().add(acceleration.x*deltaTime, acceleration.y*deltaTime, acceleration.z*deltaTime);
    mover.move(body, deltaTime);
  }
}
