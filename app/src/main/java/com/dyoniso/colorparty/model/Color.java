package com.dyoniso.colorparty.model;

public class Color {
    private int ID;
    private String name;
    private int hex;

    public Color(int id, String name, int hex) {
        this.ID = id;
        this.name = name;
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public int getHex() {
        return hex;
    }
}
