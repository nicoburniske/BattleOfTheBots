package com.burnyarosh.entity;

public class Player extends Entity {
    private boolean isWhite;

    public Player(String name, String id, boolean isWhite) {
        super(name, id);
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setIsWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }
}
