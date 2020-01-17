package piece;

public class Horse extends AbstractPiece {
    public Horse(boolean isBlack) {
        super(isBlack);
    }
    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY){
        return true;
    }

    @Override
    public boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return false;
    }
}
