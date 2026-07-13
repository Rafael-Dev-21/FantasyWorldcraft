package com.rafael04th.fwc.core;

import com.rafael04th.fwc.audio.Audio;
import com.rafael04th.fwc.input.Input;
import com.rafael04th.fwc.io.FileIO;

public interface Game {

  public Input getInput();

  public FileIO getFileIO();

  public Audio getAudio();

  public void setScreen(Screen screen);

  public Screen getCurrentScreen();

  public Screen getStartScreen();
}
