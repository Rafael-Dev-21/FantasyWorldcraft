package com.rafael04th.fwc.core;

import com.rafael04th.fwc.audio.Audio;
import com.rafael04th.fwc.input.Input;
import com.rafael04th.fwc.io.FileIO;

public interface Game {

  Input getInput();

  FileIO getFileIO();

  Audio getAudio();

  void setScreen(Screen screen);

  Screen getCurrentScreen();

  Screen getStartScreen();
}
