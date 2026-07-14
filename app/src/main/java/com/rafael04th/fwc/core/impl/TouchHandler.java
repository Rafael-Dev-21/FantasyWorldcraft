package com.rafael04th.fwc.core.impl;

import android.view.View.OnTouchListener;
import com.rafael04th.fwc.input.Input.TouchEvent;
import java.util.List;

public interface TouchHandler extends OnTouchListener {
  boolean isTouchDown(int pointer);

  int getTouchX(int pointer);

  int getTouchY(int pointer);

  List<TouchEvent> getTouchEvents();
}
