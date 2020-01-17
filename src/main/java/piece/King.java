package piece;

public class King extends AbstractPiece {
    public King(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return false;
    }
}
