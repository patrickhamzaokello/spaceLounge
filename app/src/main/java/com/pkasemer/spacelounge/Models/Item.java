package com.pkasemer.spacelounge.Models;

public class Item {

    private final int type;
    private final Object object;

    public Item(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
