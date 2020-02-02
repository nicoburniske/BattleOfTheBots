package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;

import java.util.List;

public interface IPiece {

    int getX();

    int getY();

    boolean getIsBlack();

    int getMoveCount();

    boolean getIsFirstMove();

    void makeMove(int x, int y);

    String toString();

    boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    List<Coord> getPossibleMoves(IPiece[][] board);

    IPiece copy();
}
