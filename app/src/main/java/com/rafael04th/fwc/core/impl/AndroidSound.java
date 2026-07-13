package com.rafael04th.fwc.core.impl;

import android.media.SoundPool;
import com.rafael04th.fwc.audio.Sound;

public class AndroidSound implements Sound {
  int soundId;
  SoundPool soundPool;

  AndroidSound(SoundPool soundPool, int soundId) {
    this.soundPool = soundPool;
    this.soundId = soundId;
  }

  public void play(float volume) {
    soundPool.play(soundId, volume, volume, 0, 0, 1);
  }

  public void dispose() {
    soundPool.unload(soundId);
  }
}
