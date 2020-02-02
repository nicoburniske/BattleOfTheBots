package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;

import java.util.List;

public abstract class AbstractPiece implements IPiece {
    private int x, y;
    private boolean isBlack, isFirstMove;
    private int moveCount;

    AbstractPiece(int x, int y, boolean isBlack) {
        this(x, y, isBlack, true, 0);
    }

    AbstractPiece(int x, int y, boolean isBlack, boolean isFirstMove, int moveCount) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
        this.moveCount = moveCount;
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

    public int getMoveCount() {
        return this.moveCount;
    }

    public boolean getIsFirstMove() {
        return this.isFirstMove;
    }

    public void makeMove(int x, int y) {
        this.x = x;
        this.y = y;
        this.isFirstMove = false;
        this.moveCount++;
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
        for (Coord c : new Coord(fromX, fromY).calculatePointsBetweenExclusive(new Coord(toX, toY))) {
            if (board[c.getX()][c.getY()] != null) return false;
        }
        return true;
    }


}
