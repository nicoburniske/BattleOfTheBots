package piece;

public class Queen extends AbstractPiece {

    public Queen(boolean isBlack) {
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
