package common;

public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
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
        return "(" + this.x + ", " + this.y + ")";
    }

}
