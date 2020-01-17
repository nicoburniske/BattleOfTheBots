package piece;

public class Pawn extends AbstractPiece {

    public Pawn(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return false;
    }
}
