package piece;

public class Castle extends AbstractPiece {

    public Castle(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validInlineMove(fromX, fromY, toX, toY)
                || super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }
}
