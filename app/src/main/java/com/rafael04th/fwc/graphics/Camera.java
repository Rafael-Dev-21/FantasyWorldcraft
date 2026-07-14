package com.rafael04th.fwc.graphics;

import com.rafael04th.fwc.entity.Player;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
  public static final Vector3f GLOBAL_UP = new Vector3f(0, 1, 0);

  private final Vector3f pos;
  private final Vector3f rot;

  private final Vector3f up, right, forward, target;
  private final Matrix4f viewMatrix, projMatrix, vpMatrix;
  private final float fov, near, far;
  private float aspect;

  private boolean dirty = true;
  private final Vector3f moveXZ = new Vector3f();

  public Camera() {
    pos = new Vector3f(0, 0, 0);
    rot = new Vector3f(0, 0, 0);
    fov = (float)Math.toRadians(70.5f);
    aspect = 1f;
    near = 0.1f;
    far = 100.0f;

    up = new Vector3f(0, 1, 0);
    right = new Vector3f(1, 0, 0);
    forward = new Vector3f(0, 0, -1);
    target = new Vector3f();

    viewMatrix = new Matrix4f();
    projMatrix = new Matrix4f();
    vpMatrix = new Matrix4f();
    resize(1, 1);
  }

  public void moveXZ(float dx, float dy) {
    if (dirty) {
      recalc();
    }
    moveXZ.set(right).mul(dx).add(forward.x * dy, forward.y * dy, forward.z * dy);
    moveXZ.y = 0;
    pos.add(moveXZ);
    dirty = true;
  }

  public void moveY(float dt) {
    if (dirty) {
      recalc();
    }

    pos.y = pos.y + dt;

    dirty = true;
  }

  public void turn(float dx, float dy) {
    rot.y = rot.y + dx;
    rot.x = rot.x + dy;
    if (rot.x > 89.0f) {
      rot.x = 89.0f;
    }
    if (rot.x < -89.0f) {
      rot.x = -89.0f;
    }
    dirty = true;
  }

  public void resize(float w, float h) {
    aspect = w / h;
    projMatrix.identity().perspective(fov, aspect, near, far);
    dirty = true;
  }

  private void recalc() {
    if (rot.x > 89.0f) {
      rot.x = 89.0f;
    }
    if (rot.x < -89.0f) {
      rot.x = -89.0f;
    }
    float pitchRadians = (float) Math.toRadians(rot.x);
    float yawRadians = (float) Math.toRadians(rot.y);
    forward.x = (float) Math.cos(pitchRadians) * (float) Math.sin(yawRadians);
    forward.y = (float) Math.sin(pitchRadians);
    forward.z = -(float) Math.cos(pitchRadians) * (float) Math.cos(yawRadians);
    forward.normalize();

    forward.cross(GLOBAL_UP, right).normalize();
    right.cross(forward, up).normalize();

    pos.add(forward, target);
    viewMatrix.identity().lookAt(pos, target, up);
    projMatrix.mul(viewMatrix, vpMatrix);
    dirty = false;
  }

  public Matrix4f getViewMatrix() {
    if (dirty) {
      recalc();
    }
    return viewMatrix;
  }

  public Matrix4f getProjectionMatrix() {
    return projMatrix;
  }

  public Matrix4f getCameraMatrix() {
    if (dirty) {
      recalc();
    }
    return vpMatrix;
  }
  
  public Vector3f getPosition() {
    return pos;
  }
  
  public FrustumIntersection getFrustum() {
    if (dirty)
      recalc();
    return new FrustumIntersection(vpMatrix);
  }

  public float getAspect() {
    return aspect;
  }
  
  public Vector3f getDirection() {
    if (dirty)
      recalc();
    return forward;
  }
  
  public void follow(Player p) {
    pos.set(p.getPosition());
    pos.y += 1.5f;
    rot.x = p.getPitch();
    rot.y = p.getYaw();
    dirty = true;
  }
}
