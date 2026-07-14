package com.rafael04th.fwc.core.impl;

import android.view.View.OnKeyListener;
import android.view.View;
import com.rafael04th.fwc.input.Input.KeyEvent;
import com.rafael04th.fwc.util.Pool;
import com.rafael04th.fwc.util.PoolObjectFactory;
import java.util.ArrayList;
import java.util.List;

public class KeyboardHandler implements OnKeyListener {
  final boolean[] pressedKeys = new boolean[128];
  final Pool<KeyEvent> keyEventPool;
  final List<KeyEvent> keyEventsBuffer = new ArrayList<>();
  final List<KeyEvent> keyEvents = new ArrayList<>();

  public KeyboardHandler(View view) {
    PoolObjectFactory<KeyEvent> factory =
            KeyEvent::new;
    keyEventPool = new Pool<>(factory, 100);
    view.setOnKeyListener(this);
    view.setFocusableInTouchMode(true);
    view.requestFocus();
  }

  public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
    if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE) return false;

    synchronized (this) {
      KeyEvent keyEvent = keyEventPool.newObject();
      keyEvent.keyCode = keyCode;
      keyEvent.keyChar = (char) event.getUnicodeChar();
      if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
        keyEvent.type = KeyEvent.KEY_DOWN;
        if (keyCode > 0 && keyCode < 127) pressedKeys[keyCode] = true;
      }
      if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
        keyEvent.type = KeyEvent.KEY_UP;
        if (keyCode > 0 && keyCode < 127) pressedKeys[keyCode] = false;
      }
      keyEventsBuffer.add(keyEvent);
    }
    return false;
  }

  public boolean isKeyPressed(int keyCode) {
    if (keyCode < 0 || keyCode > 127) return false;
    return pressedKeys[keyCode];
  }

  public List<KeyEvent> getKeyEvents() {
    synchronized (this) {
      int len = keyEvents.size();
      for (int i = 0; i < len; i++) {
        keyEventPool.free(keyEvents.get(i));
      }
      keyEvents.clear();
      keyEvents.addAll(keyEventsBuffer);
      keyEventsBuffer.clear();
      return keyEvents;
    }
  }
}
