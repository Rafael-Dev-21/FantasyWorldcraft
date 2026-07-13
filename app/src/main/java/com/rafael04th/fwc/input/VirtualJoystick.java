package com.rafael04th.fwc.input;

import com.rafael04th.fwc.input.Input.TouchEvent;

public class VirtualJoystick {
  public final int centerX;
  public final int centerY;
  public final float radius;
  
  private int currentX;
  private int currentY;
  private int pointerId = -1;

  public VirtualJoystick(int cx, int cy, float r) {
    centerX = cx;
    centerY = cy;
    currentX = centerX;
    currentY = centerY;
    radius = r;
  }

  public boolean update(TouchEvent te) {
    switch (te.type) {
      case TouchEvent.TOUCH_DOWN:
        if (pointerId == -1) {
          int dx = te.x - centerX;
          int dy = te.y - centerY;
          float lenSqr = dx * dx + dy * dy;
          if (lenSqr > radius * radius) {
            return false;
          }
          pointerId = te.pointer;
          currentX = te.x;
          currentY = te.y;
          return true;
        }
        break;
      case TouchEvent.TOUCH_DRAGGED:
        if (pointerId == te.pointer) {
          int dx = te.x - centerX;
          int dy = te.y - centerY;
          float lenSqr = dx * dx + dy * dy;
          if (lenSqr > radius * radius) {
            float len = (float)Math.sqrt(lenSqr);
            currentX = (int)(dx / len * radius) + centerX;
            currentY = (int)(dy / len * radius) + centerY;
          } else {
            currentX = te.x;
            currentY = te.y;
          }
          return true;
        }
        break;
      case TouchEvent.TOUCH_UP:
        if (pointerId == te.pointer) {
          pointerId = -1;
          currentX = centerX;
          currentY = centerY;
          return true;
        }
        break;
    }
    return false;
  }

  public float getX() {
    return (currentX - centerX)/radius;
  }
  public float getY() {
    return (currentY - centerY)/radius;
  }
  public float getAngle() {
    return (float)Math.atan2(currentY-centerY, currentX-centerX);
  }
  public float getStrength() {
    int dx = currentX - centerX;
    int dy = currentY - centerY;
    float lenSqr = dx * dx + dy * dy;
    return (float)Math.sqrt(lenSqr) / radius;
  }
  public boolean isPressed() {
    return pointerId != -1;
  }
  
  public int currentX() {return currentX;}
  public int currentY() {return currentY;}
}
