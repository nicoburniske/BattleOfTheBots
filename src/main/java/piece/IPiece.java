package piece;

public interface IPiece {
    boolean getIsBlack();

    boolean isValidMove(int fromX, int fromY, int toX, int toY);

    boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY);
}
