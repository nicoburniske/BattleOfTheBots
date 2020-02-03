package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.List;

public class Queen extends AbstractPiece {

    public Queen(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Queen(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return (super.validDiagonalMove(fromX, fromY, toX, toY)
                || super.validInlineMove(fromX, fromY, toX, toY))
                && super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        return null;
    }

    public IPiece copy() {
        return new Queen(super.getX(), super.getY(), super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    @Override
    public String toString() {
        return super.toString() + "Q";
    }
}
