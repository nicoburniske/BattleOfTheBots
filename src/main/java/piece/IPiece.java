package piece;

public interface IPiece {
    boolean getIsBlack();

    String toString();

    boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    int getX();
    int getY();
}
