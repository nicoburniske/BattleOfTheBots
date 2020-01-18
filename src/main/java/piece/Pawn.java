package piece;

public class Pawn extends AbstractPiece {

    public Pawn(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Pawn(int x, int y, boolean isBlack, boolean firstMove) {
        super(x, y, isBlack, firstMove);
    }

    public IPiece copy() {
        return new Pawn(super.getX(), super.getY(),super.getIsBlack(), this.getIsFirstMove());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        int direction = super.getIsBlack() ? -1 : 1;
        IPiece to = board[toX][toY];
        if (!super.getIsFirstMove()) {
            return this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
        } else {
            return isFirstMoveValid(fromX, fromY, toX, toY, direction, to)
                    || this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "P";
    }

    private boolean isValidPawnMove(int fromX, int fromY, int toX, int toY, int direction, IPiece to) {
        return (fromX == toX && (fromY + direction == toY))
                || ((Math.abs(fromX - toX) == 1) && (fromY + direction == toY) && to != null);
    }

    private boolean isFirstMoveValid(int fromX, int fromY, int toX, int toY, int direction, IPiece to) {
        return (fromX == toX) && (toY == fromY + 2 * direction) && (to == null);
    }
}
