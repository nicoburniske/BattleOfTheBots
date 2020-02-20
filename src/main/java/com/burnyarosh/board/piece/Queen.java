package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.List;

public class Queen extends AbstractPiece {

    public Queen(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    private Queen(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */

    /**
     * Checks if the move is a valid Queen move
     * @param board - current IPiece[][] board
     * @param origin - origin coordinates
     * @param target - target coordinates
     * @return - true if valid Queen move, false otherwise
     */
    @Override
    public boolean isValidMove(IPiece[][] board, Coord origin, Coord target) {
        return (super.validDiagonalMove(origin, target)
                || super.validInlineMove(origin, target))
                && super.validLineMove(board, origin, target, 8);
    }

    /**
     * Returns a list of all possible moves for this instance of Queen (including moves that danger King)
     * @param board - current IPiece[][] board
     * @param move_history - previous moves during current game
     * @return - List<Coord> where Coord is target
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        List<Coord> moves = super.getPossibleMovesRook(board);
        moves.addAll(super.getPossibleMovesBishop(board));
        return moves;
    }

    /**
     * Creates a copy of this piece
     * @return - copy of current instance of Queen
     */
    public IPiece copy() {
        return new Queen(super.getCoord().getX(), super.getCoord().getY(), super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    /**
     * toString
     * @return - "WQ" if Queen is white, "BQ" if Queen is black
     */
    @Override
    public String toString() {
        return super.toString() + "Q";
    }
}
