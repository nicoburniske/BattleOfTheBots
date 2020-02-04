package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends AbstractPiece {

    public Bishop(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Bishop(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    public IPiece copy() {
        return new Bishop(super.getX(), super.getY(),super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validDiagonalMove(fromX, fromY, toX, toY)
                && super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        return super.getPossibleMovesBishop(board);
    }

    @Override
    public  String toString() {
        return super.toString() + "B";
    }
}
