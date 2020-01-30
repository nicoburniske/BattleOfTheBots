package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPiece implements IPiece {
    private int x, y;
    private boolean isBlack, isFirstMove;

    AbstractPiece(int x, int y, boolean isBlack) {
        this(x, y, isBlack, true);
    }

    AbstractPiece(int x, int y, boolean isBlack, boolean isFirstMove) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
        this.isFirstMove = isFirstMove;
    }

    /*
    GETTERS
     */
    public boolean getIsBlack() {
        return this.isBlack;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean getIsFirstMove() {
        return this.isFirstMove;
    }

    public void makeMove(int x, int y) {
        this.x = x;
        this.y = y;
        this.isFirstMove = false;
    }

    public boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        if (this.isValidMove(board, fromX, fromY, toX, toY)) {
            this.makeMove(toX, toY);
            return true;
        } else {
            return false;
        }
    }

    public abstract boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    public abstract List<Coord> getPossibleMoves(IPiece[][] board);

    public abstract IPiece copy();

    public String toString() {
        return this.isBlack ? "B" : "W";
    }

    /**
     * checks if coordinates are aligned diagonally
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    protected boolean validDiagonalMove(int fromX, int fromY, int toX, int toY) {
        return Math.abs(fromX - toX) == Math.abs(fromY - toY);
    }

    /**
     * checks if coordinates are in same horizontal or vertical line
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    protected boolean validInlineMove(int fromX, int fromY, int toX, int toY) {
        return (fromX == toX || fromY == toY);
    }

    protected boolean validLineMove(IPiece[][] board, int fromX, int fromY, int toX, int toY, int maxDistance) {
        //checks if the distance between the points is within range
        return Math.max(Math.abs(fromX - toX), Math.abs(fromY - toY)) <= maxDistance
                //checks if there is nothing obstructing the coordinates, not the case if the distance is one
                && (maxDistance == 1 || this.notObstructed(board, fromX, fromY, toX, toY));
    }

    protected boolean notObstructed(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        for (Coord c : calculatePointsBetween(fromX, fromY, toX, toY)) {
            if (board[c.getX()][c.getY()] != null) return false;
        }
        return true;
    }

    /**
     * Calculates all Coordinates between two places on the board. Works for both diagonal and horizontal movements.
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    private List<Coord> calculatePointsBetween(int fromX, int fromY, int toX, int toY) {
        int distance = Math.max(Math.abs(fromX - toX), Math.abs(fromY - toY));
        List<Coord> res = new ArrayList<>();
        for (int i = 1; i < distance; i++) {
            res.add(new Coord(fromX + i * (toX - fromX) / distance, fromY + i * (toY - fromY) / distance));
        }
        return res;
    }
}
