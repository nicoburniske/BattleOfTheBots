package piece;

public class Queen extends AbstractPiece {

    public Queen(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return (super.validDiagonalMove(fromX, fromY, toX, toY)
                || super.validInlineMove(fromX, fromY, toX, toY))
                && super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public String toString() {
        return super.toString() + "Q";
    }
}
