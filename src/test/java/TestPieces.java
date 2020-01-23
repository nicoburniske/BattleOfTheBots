import org.junit.Before;
import piece.IPiece;

public class TestPieces {
    ChessBoard board1;
    IPiece pawnBlack, pawnWhite;
    IPiece rookBlack, rookWhite;
    IPiece knightBlack, knightWhite;
    IPiece bishopBlack, bishopWhite;
    IPiece queenBlack, queenWhite;
    IPiece kingBlack, kingWhite;

    @Before
    public void init() {
        board1 = new ChessBoard();
    }
}

