package com.pkasemer.spacelounge.Models;

public class Name {
    private final String name;
    private final int status;

    public Name(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}