package com.rafael04th.fwc.world;

import com.rafael04th.fwc.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Block {
  private static final List<Block> BLOCKS = new ArrayList<>();
  private static int count = 0;
  public final int id;
  public final int topTile;
  public final int sideTile;
  public final int bottomTile;
  
  public final boolean opaque;
  public final int dropItemId;
  
  public Block(int topTile, int sideTile, int bottomTile, boolean opaque, int dropItemId) {
    this.id = count++;
    this.topTile = topTile;
    this.sideTile = sideTile;
    this.bottomTile = bottomTile;
    this.opaque = opaque;
    this.dropItemId = dropItemId;
    BLOCKS.add(this);
  }

  public Item drop() {
    return Item.forId(dropItemId);
  }

  public static int count() { return count; }
  public static Block forId(int id) {
    if (id < 0)
      return null;
    return BLOCKS.get(id);
  }
}
