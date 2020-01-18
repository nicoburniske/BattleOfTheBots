package piece;

public interface IPiece {

    int getX();

    int getY();

    boolean getIsBlack();

    boolean getIsFirstMove();


    String toString();

    boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    boolean movePiece(IPiece[][] board, int fromX, int fromY, int toX, int toY);

    IPiece copy();
}
