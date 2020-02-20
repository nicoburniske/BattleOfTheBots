import com.burnyarosh.board.ChessBoard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestChessBoard {
    ChessBoard board1;

    @Before
    public void initExamples() {
        board1 = new ChessBoard();
    }

    @Test
    public void testBoardConstructor() {

    }

    @Test
    public void testMoveKnights() {
        //  [WHITE] - move queen-side knight
        assertTrue(board1.playGame(1, 0, 2, 2));

        //  [BLACK] - move queen-side knight
        assertTrue(board1.playGame(1, 7, 2, 5));

        //  [WHITE] - move previous knight
        assertTrue(board1.playGame(2, 2, 4, 3));

        //  [BLACK] - move king-side knight
        assertTrue(board1.playGame(6, 7, 5, 5));

        //  [WHITE] - move king-side knight
        assertTrue(board1.playGame(6, 0, 5, 2));

        //  [BLACK] - move previous knight
        assertTrue(board1.playGame(5, 5, 3, 4));

        //  [WHITE] - move knight
        assertTrue(board1.playGame(5, 2, 4, 4));

        //  [BLACK] - move knight
        assertTrue(board1.playGame(2, 5, 3, 3));

        //  [WHITE] - move knight
        assertTrue(board1.playGame(4, 4, 2, 5));

        //  [BLACK] - move knight
        assertTrue(board1.playGame(3, 3, 5, 2));
    }

    @Test
    public void testMoveKnights_Capture() {
        //  [WHITE] - move queen-side knight
        assertTrue(board1.playGame(1, 0, 2, 2));

        //  [BLACK] - move queen-side knight
        assertTrue(board1.playGame(1, 7, 2, 5));

        //  [WHITE] - move previous knight
        assertTrue(board1.playGame(2, 2, 4, 3));

        //  [BLACK] - move king-side knight
        assertTrue(board1.playGame(6, 7, 5, 5));

        //  [WHITE] - move king-side knight
        assertTrue(board1.playGame(6, 0, 5, 2));

        //  [BLACK] - move previous knight
        assertTrue(board1.playGame(5, 5, 3, 4));

        //  [WHITE] - move knight
        assertTrue(board1.playGame(5, 2, 4, 4));

        //  [BLACK] - move knight
        assertTrue(board1.playGame(2, 5, 3, 3));

        //  [WHITE] - move knight
        assertTrue(board1.playGame(4, 4, 2, 5));

        //  [BLACK] - move knight
        assertTrue(board1.playGame(3, 4, 2, 2));

        //  [WHITE] - move knight, capture black knight
        assertTrue(board1.playGame(4, 3, 2, 2));

        //  [BLACK] - move knight, capture white knight
        assertTrue(board1.playGame(3, 3, 2, 5));

        //  [WHITE] - move knight
        assertTrue(board1.playGame(2, 2, 3, 4));

        //  [BLACK] - move knight
        assertTrue(board1.playGame(2, 5, 3, 3));

        //  [WHITE] - move knight, capture pawn
        assertTrue(board1.playGame(3, 4, 4, 6));

        //  [BLACK] - move knight, capture pawn
        assertTrue(board1.playGame(3, 3, 4, 1));

        //  [WHITE] - move knight, capture bishop
        assertTrue(board1.playGame(4, 6, 2, 7));

        //  [BLACK] - move knight, capture bishop
        assertTrue(board1.playGame(4, 1, 2, 0));

        //  [WHITE] - move knight, capture pawn
        assertTrue(board1.playGame(2, 7, 0, 6));

        //  [BLACK] - move knight, capture bishop
        assertTrue(board1.playGame(2, 0, 0, 1));
    }

    @Test
    public void testCastle() {
        // move king side horse
        assertTrue(board1.playGame(6, 0, 5, 2));
        // move black pawn
        assertTrue(board1.playGame(3, 6, 3, 4));
        // move white king pawn
        assertTrue(board1.playGame(4, 1, 4, 2));
        // move black pawn
        assertTrue(board1.playGame(4, 6, 4, 4));
        // move white bishop out of the way in order to castle
        assertTrue(board1.playGame(5, 0, 3, 2));
        // move black pawn again
        assertTrue(board1.playGame(3, 4, 3, 3));
        // castle
        assertTrue(board1.playGame(4, 0, 6, 0));
    }

    // King in Check exception unit test.
    @Test
    public void testInvalidCheckMove() {
        try {
            this.board1.playGame(3, 1, 3, 2);
            this.board1.playGame(4, 6, 4, 5);
            this.board1.playGame(0, 1, 0, 2);
            this.board1.playGame(3, 7, 7, 3);
            this.board1.playGame(0, 2, 0, 3);
            this.board1.playGame(5, 7, 1, 3);
            this.board1.playGame(3, 2, 3, 3);
            fail("Exception not thrown");
        } catch (IllegalStateException e) {
            assertEquals("Move results with King in check", e.getMessage());
        }
    }

    // Black in checkmate exception and functionality unit test.
    @Test
    public void testCheckMate() {
        /*
        SCHOLARS MATE: 4 move checkmate
         */
        try {
            board1.playGame(4, 1, 4, 3);
            board1.playGame(2, 6, 2, 5);
            board1.playGame(3, 0, 7, 4);
            board1.playGame(0, 6, 0, 5);
            board1.playGame(5, 0, 2, 3);
            board1.playGame(0, 5, 0, 4);
            board1.playGame(7, 4, 5, 6);
            board1.playGame(0, 4, 0, 3);
        } catch (IllegalStateException e) {
            //TODO: make check for isGameOver() (is checkmate or stalemate)
        }
    }


    @Test
    public void testMoveQueenThroughPawn() {
        try {
            this.board1.playGame(4, 0, 4, 2);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    @Test
    public void testMoveBishopThroughPawn() {
        try {
            this.board1.playGame(2, 0, 4, 2);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    @Test
    public void testMoveKingThroughPawn() {
        try {
            this.board1.playGame(3, 0, 3, 1);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot move to square occupied by piece of same color", e.getMessage());
        }
    }

    @Test
    public void testMoveCastleThroughPawn() {
        try {
            this.board1.playGame(0, 0, 0, 2);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    @Test
    public void testEntireGame() {
        /*
         * White: Peter Yarosh
         * Black: Nick Burniske
         */
        board1.playGame(4, 1, 4, 3);
        board1.playGame(4, 6, 4, 4);
        board1.playGame(5, 0, 2, 3);
        board1.playGame(3, 6, 3, 5);
        board1.playGame(3, 0, 6, 3);
        board1.playGame(1, 7, 2, 5);
        board1.playGame(6, 3, 7, 4);
        board1.playGame(6, 7, 5, 5);
        board1.playGame(7, 4, 6, 4);
        board1.playGame(6, 6, 6, 5);
        board1.playGame(2, 3, 5, 6);
        board1.playGame(4, 7, 5, 6);
        board1.playGame(6, 4, 4, 2);
        board1.playGame(5, 5, 6, 3);
        board1.playGame(4, 2, 5, 2);
        board1.playGame(5, 6, 6, 6);
        board1.playGame(6, 0, 4, 1);
        board1.playGame(3, 5, 3, 4);
        board1.playGame(4, 3, 3, 4);
        board1.playGame(2, 5, 3, 3);
        board1.playGame(4, 1, 3, 3);
        board1.playGame(4, 4, 3, 3);
        board1.playGame(5, 2, 5, 3);
        board1.playGame(3, 3, 3, 2);
        board1.playGame(2, 1, 3, 2);
        board1.playGame(2, 7, 5, 4);
        board1.playGame(7, 1, 7, 2);
        board1.playGame(3, 7, 3, 4);
        board1.playGame(7, 2, 6, 3);
        board1.playGame(3, 4, 6, 1);
        board1.playGame(5, 3, 4, 4);
        board1.playGame(6, 6, 6, 7);
        board1.playGame(6, 3, 5, 4);
        board1.playGame(6, 1, 7, 0);
        board1.playGame(4, 0, 4, 1);
        board1.playGame(7, 0, 2, 0);
        board1.playGame(5, 4, 6, 5);
        board1.playGame(7, 6, 6, 5);
        board1.playGame(4, 4, 4, 5);
        board1.playGame(6, 7, 6, 6);
        board1.playGame(4, 5, 3, 6);
        board1.playGame(6, 6, 7, 5);
        board1.playGame(1, 0, 2, 2);
        board1.playGame(2, 0, 0, 0);
        board1.playGame(3, 6, 2, 6);
        board1.playGame(0, 7, 4, 7);
        board1.playGame(4, 1, 5, 2);
        board1.playGame(5, 7, 6, 6);
    }

}