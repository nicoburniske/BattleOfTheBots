package piece;

import common.Coord;

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
    public Coord[] possibleMoves() {
        //Coord[] moves;
        return new Coord[0];
    }

    @Override
    public  String toString() {
        return super.toString() + "B";
    }
}
