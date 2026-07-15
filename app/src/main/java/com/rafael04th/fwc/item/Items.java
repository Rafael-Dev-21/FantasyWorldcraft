package com.rafael04th.fwc.item;

import com.rafael04th.fwc.world.Blocks;

public final class Items {
  public static final Item DIRT_BLOCK = new Item("Bloco de Terra", 0, Blocks.DIRT, 72);
  public static final Item GRASS_BLOCK = new Item("Bloco de Grama", 0, Blocks.GRASS, 72);
  public static final Item SAND_BLOCK = new Item("Bloco de Areia", 0, Blocks.SAND, 72);
  public static final Item ROCK_BLOCK = new Item("Bloco de Rocha", 0, Blocks.ROCK, 72);
  public static final Item COBBLE_BLOCK = new Item("Bloco de Pedregulho", 0, Blocks.COBBLE, 72);
  public static final Item STICK = new Item("Vara", 0, null, 81);
  public static final Item PEBBLE = new Item("Pedra", 1, null, 81);
  public static final Item TEA_LEAF = new Item("Folha de Chá", 2, null, 81);
  public static final Item MANGO = new Item("Manga", 3, null, 81);

  Items() {}
}
