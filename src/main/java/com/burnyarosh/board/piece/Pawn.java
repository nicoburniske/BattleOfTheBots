package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends AbstractPiece {

    public Pawn(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    private Pawn(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */

    /**
     * Checks if the move is a valid Pawn move
     * @param board - current IPiece[][] board
     * @param origin - origin coordinates
     * @param target - target coordinates
     * @return - true if valid Pawn move, false otherwise
     */
    @Override
    public boolean isValidMove(IPiece[][] board, Coord origin, Coord target) {
        int direction = super.getIsBlack() ? -1 : 1;
        IPiece to = board[target.getX()][target.getY()];
        if (!super.getIsFirstMove()) {
            return this.isValidPawnMove(origin, target, direction, to);
        } else {
            return isFirstMoveValid(origin, target, direction, to)
                    || this.isValidPawnMove(origin, target, direction, to);
        }
    }

    /**
     * Returns a list of all possible moves for this instance of Pawn (including moves that danger King)
     * @param board - current IPiece[][] board
     * @param move_history - previous moves during current game
     * @return - List<Coord> where Coord is target
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        Coord[] skeleton = {
                new Coord(0, (super.getIsBlack() ? -1 : 1)),
                new Coord(0, (super.getIsBlack() ? -2 : 2)),
                new Coord(-1, (super.getIsBlack() ? -1 : 1)),
                new Coord(1, (super.getIsBlack() ? -1 : 1))
        };
        List<Coord> moves = new ArrayList<>();
        for (Coord c : skeleton){
            Coord temp = c.addCoords(super.getCoord());
            if (temp.isInsideBoard()){
                if (super.getIsFirstMove()){
                    if (this.isFirstMoveValid(super.getCoord(), temp,(super.getIsBlack() ? -1 : 1), board[temp.getX()][temp.getY()])){
                        moves.add(temp);
                    }
                } else {
                    if (this.isValidPawnMove(super.getCoord(), temp,(super.getIsBlack() ? -1 : 1), board[temp.getX()][temp.getY()])
                            || isValidEnPassant(super.getCoord(), temp, board, move_history)){
                        moves.add(temp);
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Creates a copy of this piece
     * @return - copy of current instance of Pawn
     */
    public IPiece copy() {
        return new Pawn(super.getCoord().getX(), super.getCoord().getY(),super.getIsBlack(), this.getIsFirstMove(), this.getMoveCount());
    }

    /**
     * toString
     * @return - "WP" if Pawn is white, "BP" if Pawn is black
     */
    @Override
    public String toString() {
        return super.toString() + "P";
    }

    /*
        ################################
            PRIVATE METHODS
        ################################
     */

    /**
     * Checks if the given move is a valid En passant move
     * @param origin - origin coordinate
     * @param target - target coordinate
     * @param board - current IPiece[][] board
     * @param move_history - List of all performed moves in current game
     * @return - true if valid En passant, false otherwise
     */
    private boolean isValidEnPassant(Coord origin, Coord target, IPiece[][] board, List<Move> move_history){
        if ( board[origin.getX()][origin.getY()] instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && board[target.getX()][target.getY()] == null){ // std conditiona
            if ( (origin.getY() == (super.getIsBlack() ? 3 : 4)) ) {    //  Condition #1
                if (move_history.size() > 3) { //  avoid OutOfBoundsException  (en passant impossible under 4 moves)
                    return (move_history.get(move_history.size() - 2).getPiece() instanceof Pawn) && (board[target.getX()][origin.getY()].getMoveCount() == 1); //Conditions 2, 3, and 4
                }
            }
        }
        return false;
    }

    /**
     * Checks if given move is a valid generic pawn move
     * @param origin - origin coordinate
     * @param target - target coordinate
     * @param direction - direction of rank (-1 if black, 1 if white)
     * @param p - IPiece of target coordinate
     * @return - true if valid Pawn move, false otherwise
     */
    private boolean isValidPawnMove(Coord origin, Coord target, int direction, IPiece p) {
        return (origin.getX() == target.getX() && (origin.getY() + direction == target.getY()) && p == null)
                || ((Math.abs(origin.getX() - target.getX()) == 1) && (origin.getY() + direction == target.getY()) && p != null);
    }

    /**
     * Checks if given move is a valid first pawn move
     * @param origin - origin coordinate
     * @param target - target coordinate
     * @param direction - direction of rank (-1 if black, 1 if white)
     * @param p - IPiece of target coordinate
     * @return - true if valid first Pawn move, false otherwise
     */
    private boolean isFirstMoveValid(Coord origin, Coord target, int direction, IPiece p) {
        return ((origin.getX() == target.getX()) && (target.getY() == origin.getY() + 2 * direction) && (p == null)) || isValidPawnMove(origin, target, direction, p);
    }

}
