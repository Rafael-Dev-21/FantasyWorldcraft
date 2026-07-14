package com.rafael04th.fwc.audio;

public interface Music {
  void play();

  void stop();

  void pause();

  void setLooping(boolean looping);

  void setVolume(float volume);

  boolean isPlaying();

  boolean isStopped();

  boolean isLooping();

  void dispose();
}
