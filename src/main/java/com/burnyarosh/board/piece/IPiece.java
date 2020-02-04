package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.List;

public interface IPiece {

    Coord getCoord();

    boolean getIsBlack();

    int getMoveCount();

    boolean getIsFirstMove();

    void makeMove(Coord c);

    String toString();

    boolean isValidMove(IPiece[][] board, Coord origin, Coord target);

    boolean movePiece(IPiece[][] board, Coord origin, Coord target);

    List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history);

    IPiece copy();
}
