package piece;

public class Knight extends AbstractPiece {
    public Knight(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return ( Math.abs(toX - fromX) != 0 && Math.abs(toY - fromY) != 0 && Math.abs(toX - fromX)+Math.abs(toY - fromY) == 3 );
    }
}
