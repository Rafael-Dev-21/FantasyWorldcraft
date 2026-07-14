package com.rafael04th.fwc.entity;

import com.rafael04th.fwc.physics.Integrator;
import com.rafael04th.fwc.physics.LeapfrogIntegrator;
import com.rafael04th.fwc.physics.WorldCollideMover;
import com.rafael04th.fwc.world.World;

public class Hare extends DynamicGameObject3D {
  private static final float HARE_THICK = 0.1f;
  private static final float HARE_HEIGHT= 0.15f;
  private static final float GRAVITY = -9.8f;
  private static final float WANDER_FORCE = 2.0f;

  public static final int HARE_SLEEP=0;
  public static final int HARE_WANDER=1;
  public static final int HARE_SEARCH=2;
  public static final int HARE_EAT=3;
  
  private int state;
  private float timer;
  private float shouldEat = 0, shouldSleep = 0;
  private float wanderAngle;

  private final Integrator integrator;
  private final WorldCollideMover mover;
  
  public Hare(float x, float y, float z, World world) {
    super(x, y, z, -HARE_THICK, 0, -HARE_THICK, HARE_THICK, HARE_HEIGHT, HARE_THICK);
    mass = 2.0f;
    state = HARE_WANDER;
    timer = 1f;
    wanderAngle = (float)(Math.random() * Math.PI * 2);

    mover = new WorldCollideMover();
    mover.setWorld(world);
    integrator = new LeapfrogIntegrator(this, mover);
  }

  public int getState() {
    return state;
  }

  public void update(float deltaTime) {
    switch (state) {
      case HARE_SLEEP:
        velocity.set(0,0,0);
        timer-=deltaTime;
        if (timer<= 0) {
          state = HARE_WANDER;
          timer = 1f;
          wanderAngle = (float)(Math.random() * Math.PI * 2);
        }
        break;

      case HARE_WANDER:
        wanderAngle += (float)(Math.random() - 0.5f) * deltaTime * 3f;

        accel.set(
            (float)Math.sin(wanderAngle) * WANDER_FORCE,
            grounded?0:GRAVITY,
            (float)Math.cos(wanderAngle) * WANDER_FORCE
        );

        integrator.move(accel, deltaTime);

        if (grounded) {
          float friction = (float) Math.pow(0.01f, deltaTime);
          velocity.x *= friction;
          velocity.z *= friction;
        }

        timer -= deltaTime;
        if (timer <= 0) {
          shouldEat += (float)(deltaTime*Math.random());
          shouldSleep += (float)(deltaTime*Math.random());
          timer=1f;
        }
        if (shouldEat >= 1f) {
          state = HARE_SEARCH;
          timer = 3f;
          velocity.set(0,0,0);
        } else if (shouldSleep >= 1f) {
          state = HARE_SLEEP;
          timer = shouldSleep * 20;
          shouldSleep = 0;
        }
        break;

      case HARE_SEARCH:
        accel.set(0,0,0);
        velocity.set(0,GRAVITY*deltaTime,0);
        if (!grounded) {
          integrator.move(accel, deltaTime);
        }
        timer -= deltaTime;
        if (timer <= 0f) {
          state = HARE_EAT;
          timer = shouldEat * 5;
          shouldEat = 0;
        }
        break;

      case HARE_EAT:
        velocity.set(0,0,0);
        timer -= deltaTime;
        if (timer <= 0f) {
          state = HARE_WANDER;
          timer = 1f;
          wanderAngle = (float)(Math.random() * Math.PI * 2);
        }
    }
  }
}
