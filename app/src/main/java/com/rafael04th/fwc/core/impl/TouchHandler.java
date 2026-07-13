package com.rafael04th.fwc.core.impl;

import android.view.View.OnTouchListener;
import com.rafael04th.fwc.input.Input.TouchEvent;
import java.util.List;

public interface TouchHandler extends OnTouchListener {
  public boolean isTouchDown(int pointer);

  public int getTouchX(int pointer);

  public int getTouchY(int pointer);

  public List<TouchEvent> getTouchEvents();
}
