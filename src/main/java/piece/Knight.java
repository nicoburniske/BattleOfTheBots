package piece;

public class Knight extends AbstractPiece {
    public Knight(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return false;
    }
}
