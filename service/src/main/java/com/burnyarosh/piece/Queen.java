package com.burnyarosh.piece;

import com.burnyarosh.common.Coord;

import java.util.List;

public class Queen extends AbstractPiece {

    public Queen(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Queen(int x, int y, boolean isBlack, boolean firstMove) {
        super(x, y, isBlack, firstMove);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return (super.validDiagonalMove(fromX, fromY, toX, toY)
                || super.validInlineMove(fromX, fromY, toX, toY))
                && super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board) {
        return null;
    }

    public IPiece copy() {
        return new Queen(super.getX(), super.getY(), super.getIsBlack(), super.getIsFirstMove());
    }

    @Override
    public String toString() {
        return super.toString() + "Q";
    }
}
