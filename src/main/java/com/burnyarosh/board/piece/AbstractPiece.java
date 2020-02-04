package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
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

    public abstract List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history);

    public abstract IPiece copy();

    public String toString() {
        return this.isBlack ? "B" : "W";
    }

    protected boolean addValidMove(IPiece[][] board, Coord c, List<Coord> moves){
        if (c.isInsideBoard()){
            if (board[c.getX()][c.getY()] == null){
                moves.add(c);
            } else {
                if (this.getIsBlack() != board[c.getX()][c.getY()].getIsBlack()){
                    moves.add(c);
                }
                return true;
            }
        }
        return false;
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

    protected List<Coord> getPossibleMovesRook(IPiece[][] board) {
        Coord self = new Coord(this.getX(), this.getY());
        boolean upBlocked = false;
        boolean rightBlocked = false;
        boolean downBlocked = false;
        boolean leftBlocked = false;
        List<Coord> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++){
            if (!upBlocked){
                upBlocked = this.addValidMove(board, new Coord(self.getX(), i).addCoords(self), moves);
            }
            if (!rightBlocked){
                rightBlocked = this.addValidMove(board, new Coord(i, self.getY()).addCoords(self), moves);
            }
            if (!downBlocked){
                downBlocked = this.addValidMove(board, new Coord(self.getX(), -i).addCoords(self), moves);
            }
            if (!leftBlocked){
                leftBlocked = this.addValidMove(board, new Coord(-i, self.getY()).addCoords(self), moves);
            }
            if (upBlocked && rightBlocked && downBlocked && leftBlocked){
                break;
            }
        }
        return moves;
    }

    protected List<Coord> getPossibleMovesBishop(IPiece[][] board) {
        Coord self = new Coord(this.getX(), this.getY());
        boolean upRightBlocked = false;
        boolean downRightBlocked = false;
        boolean downLeftBlocked = false;
        boolean upLeftBlocked = false;
        List<Coord> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++){
            if (!upRightBlocked){
                upRightBlocked = this.addValidMove(board, new Coord(i, i).addCoords(self), moves);
            }
            if (!downRightBlocked){
                downRightBlocked = this.addValidMove(board, new Coord(i, -i).addCoords(self), moves);
            }
            if (!downLeftBlocked){
                downLeftBlocked = this.addValidMove(board, new Coord(-i, -i).addCoords(self), moves);
            }
            if (!upLeftBlocked){
                upLeftBlocked = this.addValidMove(board, new Coord(-i, i).addCoords(self), moves);
            }
        }
        return moves;
    }



}
