package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.List;

public class Rook extends AbstractPiece {

    public Rook(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    private Rook(int x, int y, boolean isBlack, boolean isFirstMove, int moveCount) {
        super(x, y, isBlack, isFirstMove, moveCount);
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */

    /**
     * Checks if the move is a valid Rook move
     * @param board - current IPiece[][] board
     * @param origin - origin coordinates
     * @param target - target coordinates
     * @return - true if valid Rook move, false otherwise
     */
    @Override
    public boolean isValidMove(IPiece[][] board, Coord origin, Coord target) {
        return super.validInlineMove(origin, target)
                && super.validLineMove(board, origin, target, 8);
    }

    /**
     * Returns a list of all possible moves for this instance of Rook (including moves that danger King)
     * @param board - current IPiece[][] board
     * @param move_history - previous moves during current game
     * @return - List<Coord> where Coord is target
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        return super.getPossibleMovesRook(board);
    }

    /**
     * Creates a copy of this piece
     * @return - copy of current instance of Rook
     */
    public IPiece copy() {
        return new Rook(super.getCoord().getX(), super.getCoord().getY(), super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
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
     * @return - "WR" if Rook is white, "BR" if Rook is black
     */
    @Override
    public String toString() {
        return super.toString() + "R";
    }

}
