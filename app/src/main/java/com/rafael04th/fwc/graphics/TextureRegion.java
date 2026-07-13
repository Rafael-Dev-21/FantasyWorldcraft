package com.rafael04th.fwc.graphics;

public class TextureRegion {
  public final float u1, v1;
  public final float u2, v2;
  public final Texture texture;

  public TextureRegion(Texture texture, float x, float y, float width, float height) {
    this.u1 = x / texture.width;
    this.v1 = y / texture.height;
    this.u2 = (x + width) / texture.width;
    this.v2 = (y + height) / texture.height;
    this.texture = texture;
  }
}
