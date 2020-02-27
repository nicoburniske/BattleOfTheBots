import com.burnyarosh.board.ChessBoard;
import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.piece.Queen;
import org.junit.Before;
import com.burnyarosh.board.piece.IPiece;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
        System.out.println("TESTS ARE ALL BROKEN NEED TO BE UPDATED TO NEW STRUCTURE");
    }

    //  GENERAL PAWN MOVES

    @Test
    public void testMovePawns() {
        assertTrue(board1.isWhiteTurn());
        // move white King pawn
        assertTrue(board1.playGame(4, 1, 4, 3));

        assertFalse(board1.isWhiteTurn());
        // move black Queen pawn
        assertTrue(board1.playGame(3, 6, 3, 4));

        assertTrue(board1.isWhiteTurn());
        // move white king pawn again
        assertTrue(board1.playGame(4, 3, 4, 4));
    }

    @Test
    public void testEnPassant() {
        //  [WHITE]
        assertTrue(board1.playGame(1, 1, 1, 3));

        //  [BLACK]
        assertTrue(board1.playGame(0, 6, 0, 5));

        //  [WHITE]
        assertTrue(board1.playGame(1, 3, 1, 4));

        //  [BLACK]
        assertTrue(board1.playGame(2, 6, 2, 4));

        //  [WHITE], En Passant
        assertTrue(board1.playGame(1, 4, 2, 5));
    }

    @Test
    public void testPawnPromotion() {
        //  [WHITE]
        assertTrue(board1.playGame(1, 1, 1, 3));

        //  [BLACK]
        assertTrue(board1.playGame(0, 6, 0, 5));

        //  [WHITE]
        assertTrue(board1.playGame(1, 3, 1, 4));

        //  [BLACK]
        assertTrue(board1.playGame(2, 6, 2, 4));

        //  [WHITE], En Passant
        assertTrue(board1.playGame(1, 4, 2, 5));

        //  [BLACK]
        assertTrue(board1.playGame(3, 6, 3, 4));

        //  [WHITE]
        assertTrue(board1.playGame(2, 5, 2, 6));

        //  [BLACK]
        assertTrue(board1.playGame(2, 7, 3, 6));

        //  [WHITE]
        assertTrue(board1.playGame(2, 6, 2, 7));

        //  Check to see im Pawn Promoted to Queen
        assertTrue(board1.getBoard()[2][7] instanceof Queen);

    }

    @Test
    public void testPawnPossibleMoves() {
        Coord p1_c1 = new Coord(0, 2);
        Coord p1_c2 = new Coord(0, 3);
        assertTrue(board1.getBoard()[0][1].getPossibleMoves(board1.getBoard(), board1.getHistory()).size() == 2);
        assertTrue(board1.getBoard()[0][1].getPossibleMoves(board1.getBoard(), board1.getHistory()).contains(p1_c1));
        assertTrue(board1.getBoard()[0][1].getPossibleMoves(board1.getBoard(), board1.getHistory()).contains(p1_c2));

        // TODO: Test Pawn En passant possible moves

        //  TODO: Test Pawn Capture possible moves

    }

    //  SPECIAL PAWN MOVES

    @Test
    public void testWhitePawnStartMoveTwice() {
        try {
            // move white King pawn
            assertTrue(board1.playGame(4, 1, 4, 3));
            assertTrue(board1.playGame(3, 6, 3, 5));
            // move white King pawn twice again. Should result in IllegalArgumentException
            board1.playGame(4, 3, 4, 5);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }

    }

    @Test
    public void testWhitePawnMoveThreeSquares() {
        try {
            this.board1.playGame(4, 1, 4, 4);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    @Test
    public void testWhitePawnMoveSideways() {
        try {
            this.board1.playGame(4, 1, 4, 3);
            this.board1.playGame(4, 6, 4, 4);
            this.board1.playGame(4, 3, 5, 3);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    //Test Pawn
    //Test Promotion
    //Test En Passe
    //Test all possible moves
}

