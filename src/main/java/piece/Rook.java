package piece;

public class Rook extends AbstractPiece {

    boolean isFirstMove;

    public Rook(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Rook(int x, int y, boolean isBlack, boolean isFirstMove) {
        super(x, y, isBlack, isFirstMove);
    }

    public IPiece copy() {
        return new Rook(super.getX(), super.getY(), super.getIsBlack());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validInlineMove(fromX, fromY, toX, toY)
                || super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public String toString() {
        return super.toString() + "C";
    }


}
