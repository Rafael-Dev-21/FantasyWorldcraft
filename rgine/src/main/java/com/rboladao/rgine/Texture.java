package com.rboladao.rgine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class Texture {

  private static final String TAG = "Texture.class";

  private int rendererID;

  public Texture(Context context, String filepath) {
    InputStream is = null;

    try {
      is = context.getAssets().open(filepath);
    } catch (IOException e) {
      Log.e(TAG, "Could not load texture: " + filepath, e);
    }

    Bitmap bmp = BitmapFactory.decodeStream(is);

    int[] temp = new int[1];

    GLES30.glGenTextures(1, temp, 0);
    rendererID = temp[0];
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rendererID);
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

    GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bmp, 0);

    bmp.recycle();

    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
  }

  public void bind() {
    bind(0);
  }

  public void bind(int slot) {
    GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + slot);
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rendererID);
  }

  public void unbind() {
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
  }

  public void finalize() {
    GLES30.glDeleteTextures(1, new int[] {rendererID}, 0);
  }
}
