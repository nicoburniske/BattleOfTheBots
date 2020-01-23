package piece;

import common.Coord;

import java.util.List;

public class Knight extends AbstractPiece {
    public Knight(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Knight(int x, int y, boolean isBlack, boolean firstMove) {
        super(x, y, isBlack, firstMove);
    }

    public IPiece copy() {
        return new Knight(super.getX(), super.getY(),super.getIsBlack(), super.getIsFirstMove());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return (Math.abs(toX - fromX) != 0 && Math.abs(toY - fromY) != 0 && Math.abs(toX - fromX) + Math.abs(toY - fromY) == 3);
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board) {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "N";
    }


}
