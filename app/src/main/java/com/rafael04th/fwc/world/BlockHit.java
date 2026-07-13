package com.rafael04th.fwc.world;

public class BlockHit {
  public final float x, y, z;
  public final float normalX, normalY, normalZ;
  public final float distance;
  public final Block block;

  public BlockHit(float x, float y, float z, float normalX, float normalY, float normalZ, float distance, Block block) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.normalX = normalX;
    this.normalY = normalY;
    this.normalZ = normalZ;
    this.distance = distance;
    this.block = block;
  }
}
