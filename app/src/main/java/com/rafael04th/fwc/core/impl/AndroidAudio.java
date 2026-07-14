package com.rafael04th.fwc.core.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import com.rafael04th.fwc.audio.Audio;
import com.rafael04th.fwc.audio.Music;
import com.rafael04th.fwc.audio.Sound;
import java.io.IOException;

public class AndroidAudio implements Audio {
  final AssetManager assets;
  final SoundPool soundPool;

  AndroidAudio(Activity activity) {
    activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    assets = activity.getAssets();
    AudioAttributes attrs = new AudioAttributes.Builder()
      .setUsage(AudioAttributes.USAGE_GAME)
      .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
      .build();
    soundPool = new SoundPool.Builder()
      .setMaxStreams(20)
      .setAudioAttributes(attrs)
      .build();
  }

  public Music newMusic(String filename) {
    try {
      AssetFileDescriptor assetDescriptor = assets.openFd(filename);
      return new AndroidMusic(assetDescriptor);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load music '" + filename + "'");
    }
  }

  public Sound newSound(String filename) {
    try {
      AssetFileDescriptor assetDescriptor = assets.openFd(filename);
      int soundId = soundPool.load(assetDescriptor, 0);
      return new AndroidSound(soundPool, soundId);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load sound '" + filename + "'");
    }
  }
}
