package com.rafael04th.fwc.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.rafael04th.fwc.graphics.ShaderProgram;
import com.rafael04th.fwc.graphics.Texture;
import com.rafael04th.fwc.io.FileIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Loader {
  private FileIO fileIO;

  public Loader(FileIO fileIO) {
    this.fileIO = fileIO;
  }

  public Texture loadTexture(String filename) {
    try {
      Bitmap bmp = BitmapFactory.decodeStream(fileIO.loadAsset(filename));
      if (bmp == null) {
        return new Texture(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
      }
      return new Texture(bmp);
    } catch (IOException e) {
      return new Texture(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
    }
  }

  public String loadText(String filename) throws IOException {
    try (InputStream is = fileIO.loadAsset(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bf = new BufferedReader(isr)) {
      String line = null;
      StringBuilder sb = new StringBuilder();
      while ((line = bf.readLine()) != null) {
        sb.append(line).append('\n');
      }
      return sb.toString();
    }
  }

  public ShaderProgram loadShader(String vertName, String fragName) {
    try {
      String vertSrc = loadText(vertName);
      String fragSrc = loadText(fragName);
      return new ShaderProgram(vertSrc, fragSrc);
    } catch (IOException e) {
      return new ShaderProgram("", "");
    }
  }
}
