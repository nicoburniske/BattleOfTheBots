package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;

import java.util.List;

public class Pawn extends AbstractPiece {

    public Pawn(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Pawn(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    public IPiece copy() {
        return new Pawn(super.getX(), super.getY(),super.getIsBlack(), this.getIsFirstMove(), this.getMoveCount());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        int direction = super.getIsBlack() ? -1 : 1;
        IPiece to = board[toX][toY];
        if (!super.getIsFirstMove()) {
            return this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
        } else {
            return isFirstMoveValid(fromX, fromY, toX, toY, direction, to)
                    || this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
        }
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board) {
        /**
         * four possibilities:
         *  - pawn moves (directional) 1
         *  - pawn moves (directional) 2 (first move)
         *  - pawn moves (semi-directional (left)) 1 (capture possible) (en passent)
         *  - pawn moves (semi-directional (right)) 1 (capture possible) (en passent)
         */
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "P";
    }

    private boolean isValidPawnMove(int fromX, int fromY, int toX, int toY, int direction, IPiece to) {
        return (fromX == toX && (fromY + direction == toY) && to == null)
                || ((Math.abs(fromX - toX) == 1) && (fromY + direction == toY) && to != null);
    }

    private boolean isFirstMoveValid(int fromX, int fromY, int toX, int toY, int direction, IPiece to) {
        return (fromX == toX) && (toY == fromY + 2 * direction) && (to == null);
    }
}
