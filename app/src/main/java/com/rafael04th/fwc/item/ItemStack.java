package com.rafael04th.fwc.item;

import java.util.Objects;

public class ItemStack {
  public static final ItemStack NULL = new ItemStack(null, 0);

  private Item item;
  private int amount;
  
  public ItemStack(Item item, int amount) {
    this.item = item;
    this.amount = amount;
  }
  
  public Item item() {
    return item;
  }
  
  public int amount() {
    return amount;
  }
  
  public boolean isEmpty() {
    return item == null || amount <= 0;
  }
  
  public ItemStack copy() {
    return new ItemStack(item, amount);
  }
  
  public int add(int amount) {
    int diff = Math.max(amount + this.amount-item.maxStackSize, 0);
    this.amount = Math.min(amount+this.amount, item.maxStackSize);
    return diff;
  }
  public int remove(int amount) {
    int rest = Math.max(amount - this.amount, 0);
    this.amount = Math.max(this.amount-amount, 0);
    return rest;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (!(o instanceof ItemStack))
      return false;
    ItemStack is = (ItemStack)o;
    return item == is.item && amount == is.amount;
  }

  public int hashCode() {
    return Objects.hash(item, amount);
  }
}
