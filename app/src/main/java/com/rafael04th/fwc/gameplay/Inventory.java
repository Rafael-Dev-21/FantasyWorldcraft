package com.rafael04th.fwc.gameplay;

import com.rafael04th.fwc.item.Item;
import com.rafael04th.fwc.item.ItemStack;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Inventory {
  private final ItemStack[] SLOTS;

  public Inventory(int limit) {
    assert (limit > 0);
    SLOTS = new ItemStack[limit];
    for (int i = 0; i < limit; i++) {
      SLOTS[i] = ItemStack.NULL;
    }
  }

  public void add(ItemStack is) {
    assert (!is.isEmpty());
    int total = is.amount();
    int firstEmpty = limit();
    for (int i = 0; i < SLOTS.length && total > 0; i++) {
      ItemStack slot = SLOTS[i];
      if (slot.item() == is.item()) {
        total = slot.add(total);
        is.remove(is.amount() - total);
      }
      if (slot.isEmpty()) {
        if (firstEmpty == limit()) {
          firstEmpty = i;
        }
      }
    }
    if (total > 0) {
      SLOTS[firstEmpty] = is.copy();
    }
  }

  public void remove(ItemStack is) {
    assert (!is.isEmpty());
  }

  public boolean has(Item item) {
    assert (item != null);
    for (ItemStack is : SLOTS) {
      if (is.item() == item) {
        return true;
      }
    }
    return false;
  }

  public int count(Item item) {
    assert (item != null);
    int sum = 0;
    for (ItemStack is : SLOTS) {
      if (is.item() == item) {
        sum += is.amount();
      }
    }
    return sum;
  }

  public ItemStack get(int i) {
    assert (i >= 0);
    assert (i < limit());
    return SLOTS[i];
  }

  public void swap(int i, int j) {
    assert (i >= 0);
    assert (i < limit());
    assert (j >= 0);
    assert (j < limit());
    assert (i != j);
    
    ItemStack tmp = SLOTS[i];
    SLOTS[i] = SLOTS[j];
    SLOTS[j] = tmp;
  }

  public int limit() {
    return SLOTS.length;
  }

  public List<ItemStack> slots() {
    return List.of(SLOTS);
  }
}
