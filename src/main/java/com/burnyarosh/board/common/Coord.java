package com.burnyarosh.board.common;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(Coord coord) {
        this.x = coord.getX();
        this.y = coord.getY();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Coord addCoords(Coord c){
        return new Coord(this.x + c.getX(), this.y + c.getY());
    }

    public boolean isInsideBoard(){
        return (this.x > -1 && this.x < 8) && (this.y > -1 && this.y < 8);
    }

    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }

    /**
     * Calculates all Coordinates between two places on the board. Works for both diagonal and horizontal movements.
     * Excludes start and endpoints.
     */
    public List<Coord> calculatePointsBetweenExclusive(Coord to) {
        int distance = Math.max(Math.abs(this.getX() - to.getX()), Math.abs(this.getY() - to.getY()));
        List<Coord> res = new ArrayList<>();
        for (int i = 1; i < distance; i++) {
            res.add(new Coord(this.getX() + i * (to.getX() - this.getX()) / distance, this.getY() + i * (to.getY() - this.getY()) / distance));
        }
        return res;
    }

    /**
     * Calculates all Coordinates between two places on the board. Works for both diagonal and horizontal movements.
     * Includes endpoint.
     */
    public List<Coord> calculatePointsBetweenInclusiveEnd(Coord to) {
        List<Coord> res = this.calculatePointsBetweenExclusive(to);
        res.add(new Coord(to));
        return res;
    }

    public String toChessString(){
        return this.getFile() + this.getRank();
    }

    public JsonObject toJson() {
        JsonObject ret = new JsonObject();
        ret.put("pos", this.toChessString());
        return ret;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return this.x == coord.getX() && this.y == coord.getY();
    }
    private String getRank(){
        return String.valueOf(this.y + 1);
    }

    private String getFile() {
        return String.valueOf((char) (97 + this.x));
    }
}
