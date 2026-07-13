package com.rafael04th.fwc.input;

import com.rafael04th.fwc.input.Input.TouchEvent;

public class OrbitControl {
  private int lastX = -1;
  private int lastY = -1;
  private int deltaX = 0, deltaY = 0;
  private int pointer = -1;

  public boolean update(float deltaTime, TouchEvent touch) {
    switch (touch.type) {
      case TouchEvent.TOUCH_DOWN:
        if (pointer == -1) {
          pointer = touch.pointer;
          lastX = touch.x;
          lastY = touch.y;
          return true;
        }
        break;
      case TouchEvent.TOUCH_DRAGGED:
        if (pointer == touch.pointer) {
          deltaX = touch.x - lastX;
          deltaY = lastY - touch.y;
          lastX = touch.x;
          lastY = touch.y;
          return true;
        }
        break;
      case TouchEvent.TOUCH_UP:
        if (pointer == touch.pointer) {
          deltaX = 0;
          deltaY = 0;
          pointer = -1;
          return true;
        }
    }
    return false;
  }

  public boolean isPressed() {
    return pointer != -1;
  }

  public int getX() {
    return deltaX;
  }
  public int getY() {
    return deltaY;
  }
}
