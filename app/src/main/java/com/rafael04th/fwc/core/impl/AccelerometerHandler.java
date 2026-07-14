package com.rafael04th.fwc.core.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerHandler implements SensorEventListener {
  float accelX;
  float accelY;
  float accelZ;
  final SensorManager manager;

  AccelerometerHandler(Context context) {
    manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (accel != null) {
      manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
    }
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {}

  public void onSensorChanged(SensorEvent event) {
    accelX = event.values[0];
    accelY = event.values[1];
    accelZ = event.values[2];
  }

  public float getAccelX() {
    return accelX;
  }

  public float getAccelY() {
    return accelY;
  }

  public float getAccelZ() {
    return accelZ;
  }

  public void dispose() {
    manager.unregisterListener(this);
  }
}
