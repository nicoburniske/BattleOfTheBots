package piece;

public class Castle extends AbstractPiece {

    public Castle(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public IPiece copy() {
        return new Castle(super.getX(), super.getY(),super.getIsBlack());
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
