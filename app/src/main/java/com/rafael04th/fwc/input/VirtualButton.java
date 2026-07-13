package com.rafael04th.fwc.input;

import com.rafael04th.fwc.input.Input.TouchEvent;

public class VirtualButton {
  
  private int pointer = -1;
  public final int x, y, width, height;

  public VirtualButton(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public boolean update(TouchEvent touch) {
    switch (touch.type) {
      case TouchEvent.TOUCH_DOWN:
        if (pointer == -1) {
          if (touch.x >= x && touch.y >= y && touch.x < (x + width) && touch.y < (y + height)) {
            pointer = touch.pointer;
            return true;
          }
        }
        break;
      case TouchEvent.TOUCH_UP:
        if (pointer == touch.pointer) {
          pointer = -1;
          return true;
        }
        break;
      case TouchEvent.TOUCH_DRAGGED:
        return pointer == touch.pointer;
    }
    return false;
  }

  public boolean isPressed() {
    return pointer != -1;
  }
}
