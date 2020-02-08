package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.List;

public class Bishop extends AbstractPiece {

    public Bishop(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    private Bishop(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */

    /**
     * Checks if the move is a valid Bishop move
     * @param board - current IPiece[][] board
     * @param origin - origin coordinates
     * @param target - target coordinates
     * @return - true if valid Bishop move, false otherwise
     */
    @Override
    public boolean isValidMove(IPiece[][] board, Coord origin, Coord target) {
        return super.validDiagonalMove(origin, target)
                && super.validLineMove(board, origin, target, 8);
    }

    /**
     * Returns a list of all possible moves for this instance of Bishop (including moves that danger King)
     * @param board - current IPiece[][] board
     * @param move_history - previous moves during current game
     * @return - List<Coord> where Coord is target
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        return super.getPossibleMovesBishop(board);
    }

    /**
     * Creates a copy of this piece
     * @return - copy of current instance of Bishop
     */
    public IPiece copy() {
        return new Bishop(super.getCoord().getX(), super.getCoord().getY(),super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
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
     * @return - "WB" if Bishop is white, "BB" if Bishop is black
     */
    @Override
    public  String toString() {
        return super.toString() + "B";
    }
}
