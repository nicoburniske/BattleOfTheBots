package piece;

import common.Coord;

public interface IPiece {

    int getX();

    int getY();

    boolean getIsBlack();

    boolean getIsFirstMove();

    void makeMove(int x, int y);

    String toString();

    boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    Coord[] possibleMoves();

    IPiece copy();
}
