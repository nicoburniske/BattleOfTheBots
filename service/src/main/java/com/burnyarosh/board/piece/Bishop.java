package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;

import java.util.List;

public class Bishop extends AbstractPiece {

    public Bishop(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Bishop(int x, int y, boolean isBlack, boolean firstMove) {
        super(x, y, isBlack, firstMove);
    }

    public IPiece copy() {
        return new Bishop(super.getX(), super.getY(),super.getIsBlack(), super.getIsFirstMove());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validDiagonalMove(fromX, fromY, toX, toY)
                && super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board) {
        return null;
    }

    @Override
    public  String toString() {
        return super.toString() + "B";
    }
}
