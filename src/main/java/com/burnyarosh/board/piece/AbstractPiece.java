package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPiece implements IPiece {
    private Coord c;
    private boolean isBlack, isFirstMove;
    private int moveCount;

    AbstractPiece(int x, int y, boolean isBlack) {
        this(x, y, isBlack, true, 0);
    }

    AbstractPiece(int x, int y, boolean isBlack, boolean isFirstMove, int moveCount) {
        this.c = new Coord(x, y);
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

    public Coord getCoord(){
        return this.c;
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public boolean getIsFirstMove() {
        return this.isFirstMove;
    }

    public void makeMove(Coord c) {
        this.c = c;
        this.isFirstMove = false;
        this.moveCount++;
    }

    public boolean movePiece(IPiece[][] board, Coord origin, Coord target) {
        if (this.isValidMove(board, origin, target)) {
            this.makeMove(target);
            return true;
        } else {
            return false;
        }
    }

    public abstract boolean isValidMove(IPiece[][] board, Coord origin, Coord target);

    public abstract List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history);

    public abstract IPiece copy();

    public JsonObject toJson() {
        JsonObject ret = new JsonObject();
        ret.put("piece", this.toString());
        ret.put("pos", this.c.toChessString());
        return ret;
    }

    public String toString() {
        return this.isBlack ? "B" : "W";
    }

    /**
     * Checks if the given move is valid, if so adds it to List<Coord>
     * @param board - current IPiece[][] board
     * @param c - target coordinate
     * @param moves - List<Coord> of potential target coordinates for an individual piece
     * @return - true if move results in a Line of Sight block, false otherwise
     */
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
     * Checks if coordinates are aligned diagonally
     * @param origin - origin coordinate
     * @param target - target coordinate
     * @return - true if valid diagonal move, false otherwise
     */
    protected boolean validDiagonalMove(Coord origin, Coord target) {
        return Math.abs(origin.getX() - target.getX()) == Math.abs(origin.getY() - target.getY());
    }

    /**
     * Checks if coordinates are in same horizontal or vertical line
     * @param origin - origin coordinate
     * @param target - target coordinate
     * @return - true if valid in-line move, false otherwise
     */
    protected boolean validInlineMove(Coord origin, Coord target) {
        return (origin.getX() == target.getX() || origin.getY() == target.getY());
    }

    protected boolean validLineMove(IPiece[][] board, Coord origin, Coord target, int maxDistance) {
        //checks if the distance between the points is within range
        return Math.max(Math.abs(origin.getX() - target.getX()), Math.abs(origin.getY() - target.getY())) <= maxDistance
                //checks if there is nothing obstructing the coordinates, not the case if the distance is one
                && (maxDistance == 1 || this.notObstructed(board, origin, target));
    }

    protected boolean notObstructed(IPiece[][] board, Coord origin, Coord target) {
        for (Coord c : origin.calculatePointsBetweenExclusive(target)) {
            if (board[c.getX()][c.getY()] != null) return false;
        }
        return true;
    }

    protected List<Coord> getPossibleMovesRook(IPiece[][] board) {
        boolean upBlocked = false;
        boolean rightBlocked = false;
        boolean downBlocked = false;
        boolean leftBlocked = false;
        List<Coord> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++){
            if (!upBlocked){
                upBlocked = this.addValidMove(board, new Coord(0, i).addCoords(this.c), moves);
            }
            if (!rightBlocked){
                rightBlocked = this.addValidMove(board, new Coord(i, 0).addCoords(this.c), moves);
            }
            if (!downBlocked){
                downBlocked = this.addValidMove(board, new Coord(0, -i).addCoords(this.c), moves);
            }
            if (!leftBlocked){
                leftBlocked = this.addValidMove(board, new Coord(-i, 0).addCoords(this.c), moves);
            }
            if (upBlocked && rightBlocked && downBlocked && leftBlocked){
                break;
            }
        }
        return moves;
    }

    protected List<Coord> getPossibleMovesBishop(IPiece[][] board) {
        boolean upRightBlocked = false;
        boolean downRightBlocked = false;
        boolean downLeftBlocked = false;
        boolean upLeftBlocked = false;
        List<Coord> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++){
            if (!upRightBlocked){
                upRightBlocked = this.addValidMove(board, new Coord(i, i).addCoords(this.c), moves);
            }
            if (!downRightBlocked){
                downRightBlocked = this.addValidMove(board, new Coord(i, -i).addCoords(this.c), moves);
            }
            if (!downLeftBlocked){
                downLeftBlocked = this.addValidMove(board, new Coord(-i, -i).addCoords(this.c), moves);
            }
            if (!upLeftBlocked){
                upLeftBlocked = this.addValidMove(board, new Coord(-i, i).addCoords(this.c), moves);
            }
        }
        return moves;
    }

}