import common.Coord;
import piece.IPiece;
import piece.Queen;

public class Main {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        System.out.println(board);
    }
    //TODO: figure out castling logic. Only one part missing. Description in Castle Class.
    //TODO: isInCheck() method in ChessBoard class. if isInCheck, and the move does not remove the player from check it is an invalid move.
    //TODO: make sure that the play game method ensures that the move will not endanger user king,
    //TODO: create isGameOver method: () -> boolean
    //TODO: create possibleMove Method: () -> List<Pair<Coord>>
    //TODO: figure out enpasse logic
}
