package com.rafael04th.fwc.graphics;

import android.graphics.Bitmap;
import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;

public class Texture {
  private final int texture;
  public final int width;
  public final int height;

  public Texture(Bitmap bmp) {
    int[] ids = new int[1];
    glGenTextures(1, ids, 0);
    glBindTexture(GL_TEXTURE_2D, ids[0]);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    texImage2D(GL_TEXTURE_2D, 0, bmp, 0);
    width = bmp.getWidth();
    height = bmp.getHeight();
    bmp.recycle();
    texture = ids[0];
  }

  public void bind() {
    bind(0);
  }

  public void bind(int index) {
    glActiveTexture(GL_TEXTURE0 + index);
    glBindTexture(GL_TEXTURE_2D, texture);
  }

  public void unbind() {
    glBindTexture(GL_TEXTURE_2D, 0);
  }

  public void parameteri(int pname, int param) {
    glTexParameteri(GL_TEXTURE_2D, pname, param);
  }
  
  public void dispose() {
    glDeleteTextures(1, new int[]{texture}, 0);
  }
}
