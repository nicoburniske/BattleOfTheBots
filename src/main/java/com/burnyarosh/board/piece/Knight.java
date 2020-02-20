package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class Knight extends AbstractPiece {
    public Knight(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Knight(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */

    /**
     * Checks if the move is a valid Knight move
     * @param board - current IPiece[][] board
     * @param origin - origin coordinates
     * @param target - target coordinates
     * @return - true if valid Knight move, false otherwise
     */
    @Override
    public boolean isValidMove(IPiece[][] board, Coord origin, Coord target) {
        return (Math.abs(target.getX() - origin.getX()) != 0 && Math.abs(target.getY() - origin.getY()) != 0 && Math.abs(target.getX() - origin.getX()) + Math.abs(target.getY() - origin.getY()) == 3);
    }

    /**
     * Returns a list of all possible moves for this instance of Knight (including moves that danger King)
     * @param board - current IPiece[][] board
     * @param move_history - previous moves during current game
     * @return - List<Coord> where Coord is target
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        Coord[] skeleton = {
                new Coord(-1, 2), new Coord(1, 2),
                new Coord(2, -1), new Coord(2, 1),
                new Coord(-1, -2), new Coord(1, -2),
                new Coord(-2, -1), new Coord(-2, 1)
        };
        List<Coord> moves = new ArrayList<>();
        for (Coord c : skeleton){
            addValidMove(board, c.addCoords(super.getCoord()), moves);
        }
        return moves;
    }

    /**
     * Creates a copy of this piece
     * @return - copy of current instance of Knight
     */
    public IPiece copy() {
        return new Knight(super.getCoord().getX(), super.getCoord().getY(),super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    /**
     * Piece cannot promote --> return copy of itself
     * @return - copy of self
     */
    @Override
    public IPiece promote(boolean toKnight) {
        return this.copy();
    }

    /**
     * toString
     * @return - "WN" if Knight is white, "BN" if Knight is black
     */
    @Override
    public String toString() {
        return super.toString() + "N";
    }

}
