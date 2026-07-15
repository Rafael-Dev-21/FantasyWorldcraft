package com.rafael04th.fwc.item;

import com.rafael04th.fwc.graphics.TextureRegion;
import com.rafael04th.fwc.graphics.Texture;
import com.rafael04th.fwc.world.Block;
import java.util.ArrayList;
import java.util.List;

public class Item {
  private static final int LEN = 16;
  private final static List<Item> ITEMS = new ArrayList<>();
  private static int count = 0;

  public final int id;
  public final int icon;
  public final int maxStackSize;
  // Can be null
  public final int blockId;
  public final String name;
  
  private TextureRegion region = null;

  Item(String name, int icon, int blockId, int maxStackSize) {
    this.id = count++;
    this.name = name;
    this.icon = icon;
    this.blockId = blockId;
    this.maxStackSize = maxStackSize;
    ITEMS.add(this);
  }

  private void load(Texture texture) {
    int cols = texture.width / LEN;
    region = new TextureRegion(texture, icon % cols, icon / cols, LEN, LEN);
  }

  public TextureRegion region() {
    return region;
  }

  public Block block() {
    return Block.forId(blockId);
  }

  public static Item forId(int id) {
    if (id < 0)
      return null;
    return ITEMS.get(id);
  }

  public static int count() {
    return count;
  }

  public static void loadAll(Texture texture) {
    for (Item item : ITEMS) item.load(texture);
  }
}
