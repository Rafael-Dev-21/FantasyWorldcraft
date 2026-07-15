package com.rafael04th.fwc.world;

public final class Blocks {
  public static final Block AIR = new Block(0, 0, 0, false, -1);
  public static final Block DIRT = new Block(2, 2, 2, true, 0);
  public static final Block GRASS = new Block(0, 1, 2, true, 0);
  public static final Block SNOW = new Block(3, 4, 2, true, 0);
  public static final Block SAND = new Block(5,5,5, true, 2);
  public static final Block ROCK = new Block(6, 6, 6, true, 4);
  public static final Block COBBLE = new Block(7, 7, 7, true, 4);
}
