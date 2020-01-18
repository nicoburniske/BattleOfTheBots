package piece;

public class Bishop extends AbstractPiece {

    public Bishop(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validDiagonalMove(fromX, fromY, toX, toY)
                && super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }
    @Override
    public String toString() {
        return super.toString() + "B";
    }
}
