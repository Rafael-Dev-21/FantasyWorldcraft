package com.rafael04th.fwc.entity;

import com.rafael04th.fwc.graphics.Camera;
import com.rafael04th.fwc.input.OrbitControl;
import com.rafael04th.fwc.input.VirtualButton;
import com.rafael04th.fwc.input.VirtualJoystick;
import com.rafael04th.fwc.physics.Integrator;
import com.rafael04th.fwc.physics.KinematicBody;
import com.rafael04th.fwc.physics.LeapfrogIntegrator;
import com.rafael04th.fwc.physics.WorldCollideMover;
import com.rafael04th.fwc.world.AABB;
import com.rafael04th.fwc.world.Block;
import com.rafael04th.fwc.world.Blocks;
import com.rafael04th.fwc.world.World;
import java.util.Optional;
import org.joml.Vector3f;

public class Player extends DynamicGameObject3D {
  private static final float GRAVITY = -9.8f;
  private float yaw, pitch;
  private float moveSpeed = 36.5f;
  private float turnSpeed = 6.0f;

  private Integrator leapfrog;
  private WorldCollideMover mover;

  public Player() {
    super(0,0,0, -0.25f, 0f, -0.25f, 0.25f, 1.75f, 0.25f);
    mass = 80.0f;
    mover = new WorldCollideMover();
    leapfrog = new LeapfrogIntegrator(this, mover);
  }

  public float getYaw() {
    return yaw;
  }
  public float getPitch() {
    return pitch;
  }
  
  public void update(float deltaTime, VirtualJoystick joystick, OrbitControl orbit, VirtualButton jumpButton, World world, Camera fpsCamera) {
    mover.setWorld(world);
    
    float jx = 0, jy = 0;
    if (joystick.isPressed()) {
      jx = joystick.getX();
      jy = joystick.getY();
    }
    int ox = 0, oy = 0;
    if (orbit.isPressed()) {
      ox = orbit.getX();
      oy = orbit.getY();
    }
    
    update(deltaTime, jx, -jy, ox, oy, jumpButton.isPressed(), world);
    fpsCamera.follow(this);
  }
  
  // TODO!: maybe params object?
  public void update(float deltaTime, float joystickX, float joystickY, float orbitX, float orbitY, boolean jump, World world) {
    yaw = yaw + orbitX * deltaTime * turnSpeed;
    pitch = pitch + orbitY * deltaTime * turnSpeed;
    pitch = Math.max(-89.0f, Math.min(89.0f, pitch));
    
    float yawRad = (float)Math.toRadians(yaw);
    float forwardX = (float)Math.sin(yawRad);
    float forwardZ = -(float)Math.cos(yawRad);
    float rightX = -forwardZ;
    float rightZ = forwardX;

    accel.set(
        (forwardX * joystickY + rightX * joystickX) * moveSpeed,
        grounded ? 0 : GRAVITY,
        (forwardZ * joystickY + rightZ * joystickX) * moveSpeed
    );
    
    if (jump && grounded) {
      velocity.y = 5f;
      grounded = false;
    }
      
    leapfrog.move(accel, deltaTime);
 
    float friction = (float)Math.pow(grounded ? 0.001f : 0.002f, deltaTime);
    velocity.x *= friction;
    velocity.z *= friction;
  }
}
