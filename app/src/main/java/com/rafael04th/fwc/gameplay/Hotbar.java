package com.rafael04th.fwc.gameplay;

import com.rafael04th.fwc.world.Block;

public class Hotbar {
    private final Block[] HOTBAR;
    private int index = 0;

    public Hotbar(final Block... blocks) {
        HOTBAR = blocks;
    }

    public Block  current() {
        return HOTBAR[index];
    }

    public void next() {
        index = (index + 1) % HOTBAR.length;
    }

    public int index() {
        return index;
    }
}
