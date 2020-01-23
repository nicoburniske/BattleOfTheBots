package piece;

import common.Coord;

import java.util.List;

public class Rook extends AbstractPiece {

    public Rook(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Rook(int x, int y, boolean isBlack, boolean isFirstMove) {
        super(x, y, isBlack, isFirstMove);
    }

    public IPiece copy() {
        return new Rook(super.getX(), super.getY(), super.getIsBlack(), super.getIsFirstMove());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validInlineMove(fromX, fromY, toX, toY)
                || super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board) {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "C";
    }

}
