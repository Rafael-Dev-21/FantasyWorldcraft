package com.rafael04th.fwc.gameplay;

import com.rafael04th.fwc.item.ItemStack;

public class Hotbar {
    private final Inventory BASE;
    private final int START;
    private final int LENGTH;
    private int index = 0;

    public Hotbar(Inventory base, int start, int length) {
        assert (base != null);
        assert (start >= 0);
        assert (length > 0);
        BASE = base;
        START = start;
        LENGTH = length;
    }

    public ItemStack current() {
        return BASE.get(index + START);
    }

    public void next() {
        index = (index + 1) % LENGTH;
    }

    public int index() {
        return index;
    }
}
