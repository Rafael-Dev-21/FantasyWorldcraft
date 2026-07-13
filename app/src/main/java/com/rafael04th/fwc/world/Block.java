package com.rafael04th.fwc.world;

public class Block {
  
  public final int topTile;
  public final int sideTile;
  public final int bottomTile;
  
  public final boolean opaque;
  
  public Block(int topTile, int sideTile, int bottomTile, boolean opaque) {
    this.topTile = topTile;
    this.sideTile = sideTile;
    this.bottomTile = bottomTile;
    this.opaque = opaque;
  }
}
