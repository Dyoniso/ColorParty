package com.dyoniso.colorparty.model;

public class Color {
    private int ID;
    private String name;

    public Color(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }
}
