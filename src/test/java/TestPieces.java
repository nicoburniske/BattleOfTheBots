import com.burnyarosh.board.Chess;
import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.piece.Queen;
import org.junit.Before;
import org.junit.Test;

import static com.burnyarosh.board.Chess.Color.BLACK;
import static com.burnyarosh.board.Chess.Color.WHITE;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestPieces {
    private Chess board1;
    //IPiece pawnBlack, pawnWhite;
    //IPiece rookBlack, rookWhite;
    //IPiece knightBlack, knightWhite;
    //IPiece bishopBlack, bishopWhite;
    //IPiece queenBlack, queenWhite;
    //IPiece kingBlack, kingWhite;

    @Before
    public void init() {
        board1 = new Chess();
    }

    //  GENERAL PAWN MOVES

    @Test
    public void testMovePawns() {
        assertEquals(WHITE, board1.getTurn());
        // move white King pawn
        assertTrue(board1.play(4, 1, 4, 3));

        assertEquals(BLACK, board1.getTurn());
        // move black Queen pawn
        assertTrue(board1.play(3, 6, 3, 4));

        assertEquals(WHITE, board1.getTurn());
        // move white king pawn again
        assertTrue(board1.play(4, 3, 4, 4));
    }

    @Test
    public void testEnPassant() {
        //  [WHITE]
        assertTrue(board1.play(1, 1, 1, 3));

        //  [BLACK]
        assertTrue(board1.play(0, 6, 0, 5));

        //  [WHITE]
        assertTrue(board1.play(1, 3, 1, 4));

        //  [BLACK]
        assertTrue(board1.play(2, 6, 2, 4));

        //  [WHITE], En Passant
        assertTrue(board1.play(1, 4, 2, 5));
    }

    @Test
    public void testPawnPromotion() {
        //  [WHITE]
        assertTrue(board1.play(1, 1, 1, 3));

        //  [BLACK]
        assertTrue(board1.play(0, 6, 0, 5));

        //  [WHITE]
        assertTrue(board1.play(1, 3, 1, 4));

        //  [BLACK]
        assertTrue(board1.play(2, 6, 2, 4));

        //  [WHITE], En Passant
        assertTrue(board1.play(1, 4, 2, 5));

        //  [BLACK]
        assertTrue(board1.play(3, 6, 3, 4));

        //  [WHITE]
        assertTrue(board1.play(2, 5, 2, 6));

        //  [BLACK]
        assertTrue(board1.play(2, 7, 3, 6));

        //  [WHITE]
        assertTrue(board1.play(2, 6, 2, 7));

        //  Check to see im Pawn Promoted to Queen
        assertTrue(board1.getBoard().getBoardArray()[2][7] instanceof Queen);

    }

    @Test
    public void testPawnPossibleMoves() {
        Coord p1_c1 = new Coord(0, 2);
        Coord p1_c2 = new Coord(0, 3);
        assertEquals(2, board1.getBoard().getBoardArray()[0][1].getPossibleMoves(board1.getBoard().getBoardArray(), board1.getBoard().getMoveHistory()).size());
        assertTrue(board1.getBoard().getBoardArray()[0][1].getPossibleMoves(board1.getBoard().getBoardArray(), board1.getBoard().getMoveHistory()).contains(p1_c1));
        assertTrue(board1.getBoard().getBoardArray()[0][1].getPossibleMoves(board1.getBoard().getBoardArray(), board1.getBoard().getMoveHistory()).contains(p1_c2));

        // TODO: Test Pawn En passant possible moves

        //  TODO: Test Pawn Capture possible moves

    }

    //  SPECIAL PAWN MOVES

    @Test
    public void testWhitePawnStartMoveTwice() {
        try {
            // move white King pawn
            assertTrue(board1.play(4, 1, 4, 3));
            assertTrue(board1.play(3, 6, 3, 5));
            // move white King pawn twice again. Should result in IllegalArgumentException
            board1.play(4, 3, 4, 5);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }

    }

    @Test
    public void testWhitePawnMoveThreeSquares() {
        try {
            this.board1.play(4, 1, 4, 4);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    @Test
    public void testWhitePawnMoveSideways() {
        try {
            this.board1.play(4, 1, 4, 3);
            this.board1.play(4, 6, 4, 4);
            this.board1.play(4, 3, 5, 3);
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

