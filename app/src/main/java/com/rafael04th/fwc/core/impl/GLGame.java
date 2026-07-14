package com.rafael04th.fwc.core.impl;

import android.app.Activity;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.rafael04th.fwc.audio.Audio;
import com.rafael04th.fwc.core.Game;
import com.rafael04th.fwc.core.Screen;
import com.rafael04th.fwc.input.Input;
import com.rafael04th.fwc.io.FileIO;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class GLGame extends Activity implements Game, Renderer {
  enum GLGameState {
    Initialized,
    Running,
    Paused,
    Finished,
    Idle
  }

  GLSurfaceView glView;
  GLGraphics glGraphics;

  Audio audio;
  Input input;
  FileIO fileIO;
  Screen screen;
  GLGameState state = GLGameState.Initialized;
  final Object stateChanged = new Object();
  long startTime = System.nanoTime();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    glView = new GLSurfaceView(this);
    glView.setEGLContextClientVersion(2);
    glView.setRenderer(this);
    setContentView(glView);

    glGraphics = new GLGraphics(glView);
    fileIO = new AndroidFileIO(this);
    audio = new AndroidAudio(this);
    input = new AndroidInput(this, glView, 1, 1);
  }

  @Override
  public void onResume() {
    super.onResume();
    glView.onResume();
  }

  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    glGraphics.setGL(gl);

    synchronized (stateChanged) {
      if (state == GLGameState.Initialized)
        screen = getStartScreen();
      state = GLGameState.Running;
      screen.resume();
      startTime = System.nanoTime();
    }
  }

  public void onSurfaceChanged(GL10 gl, int width, int height) {}

  public void onDrawFrame(GL10 gl) {
    GLGameState state = null;
    
    synchronized (stateChanged) {
      state = this.state;
    }

    if (state == GLGameState.Running) {
      float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
      startTime = System.nanoTime();

      screen.update(deltaTime);
      screen.present(deltaTime);
    }
    if (state == GLGameState.Paused) {
      screen.pause();
      synchronized (stateChanged) {
        this.state = GLGameState.Idle;
        stateChanged.notifyAll();
      }
    }

    if (state == GLGameState.Finished) {
      screen.pause();
      screen.dispose();
      synchronized (stateChanged) {
        this.state = GLGameState.Idle;
        stateChanged.notifyAll();
      }
    }
  }

  @Override
  public void onPause() {
    synchronized (stateChanged) {
      if (isFinishing())
        state = GLGameState.Finished;
      else
        state = GLGameState.Paused;
      while (true) {
        try {
          stateChanged.wait();
          break;
        } catch (InterruptedException e) {
          Log.e("GLGame.class", "Could not pause/finish game", e);
        }
      }
    }
    glView.onPause();
    super.onPause();
  }
  
  @Override
  public void onDestroy() {
    ((AndroidInput)getInput()).dispose();
    super.onDestroy();
  }

  public GLGraphics getGLGraphics() {
    return glGraphics;
  }

  public Input getInput() {
    return input;
  }

  public FileIO getFileIO() {
    return fileIO;
  }

  public Audio getAudio() {
    return audio;
  }

  public void setScreen(Screen newScreen) {
    if (screen == null)
      throw new IllegalArgumentException("Screen must not be null");

    this.screen.pause();
    this.screen.dispose();
    newScreen.resume();
    newScreen.update(0);
    this.screen = newScreen;
  }

  public Screen getCurrentScreen() {
    return screen;
  }
}
