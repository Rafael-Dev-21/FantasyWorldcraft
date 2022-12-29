package com.rboladao.fantasyworldcraft;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.rboladao.fantasyworldcraft.GameRenderer;

public class GameView extends GLSurfaceView {

  private final GameRenderer renderer;
  private float lastX, lastY;

  public GameView(Context context) {
    super(context);
    setEGLContextClientVersion(3);

    renderer = new GameRenderer(context);

    setRenderer(renderer);
  }

  public GameView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setEGLContextClientVersion(3);

    renderer = new GameRenderer(context);

    setRenderer(renderer);
  }

  public boolean onTouchEvent(final MotionEvent e) {
    float x = e.getX();
    float y = e.getY();

    switch (e.getAction()) {
      case MotionEvent.ACTION_MOVE:
        float dx = lastX - x;
        float dy = y - lastY;

        renderer.rotCam(dx, dy);
    }

    lastX = x;
    lastY = y;

    return true;
  }
}
