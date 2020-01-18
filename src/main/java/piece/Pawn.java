package piece;

public class Pawn extends AbstractPiece {
    private boolean firstMove;

    public Pawn(boolean isBlack) {
        super(isBlack);
        firstMove = true;
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        int direction = super.getIsBlack() ? -1 : 1;
        IPiece to = board[toX][toY];
        if (!this.firstMove) {
            return this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
        } else {
            boolean res = isFirstMoveValid(fromX, fromY, toX, toY, direction, to)
                    || this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
            if (res) this.firstMove = false;
            return res;
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
