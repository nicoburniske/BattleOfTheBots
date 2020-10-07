import com.burnyarosh.board.Chess;
import com.burnyarosh.board.common.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestChessBoard {
    private Chess board1;

    @BeforeEach
    public void initExamples() {
        board1 = new Chess();
    }

    @Test
    public void testBoardConstructor() {

    }

    @Test
    public void testMoveKnights() {
        //  [WHITE] - move queen-side knight
        assertTrue(board1.play(1, 0, 2, 2));

        //  [BLACK] - move queen-side knight
        assertTrue(board1.play(1, 7, 2, 5));

        //  [WHITE] - move previous knight
        assertTrue(board1.play(2, 2, 4, 3));

        //  [BLACK] - move king-side knight
        assertTrue(board1.play(6, 7, 5, 5));

        //  [WHITE] - move king-side knight
        assertTrue(board1.play(6, 0, 5, 2));

        //  [BLACK] - move previous knight
        assertTrue(board1.play(5, 5, 3, 4));

        //  [WHITE] - move knight
        assertTrue(board1.play(5, 2, 4, 4));

        //  [BLACK] - move knight
        assertTrue(board1.play(2, 5, 3, 3));

        //  [WHITE] - move knight
        assertTrue(board1.play(4, 4, 2, 5));

        //  [BLACK] - move knight
        assertTrue(board1.play(3, 3, 5, 2));
    }

    @Test
    public void testMoveKnights_Capture() {
        //  [WHITE] - move queen-side knight
        assertTrue(board1.play(1, 0, 2, 2));

        //  [BLACK] - move queen-side knight
        assertTrue(board1.play(1, 7, 2, 5));

        //  [WHITE] - move previous knight
        assertTrue(board1.play(2, 2, 4, 3));

        //  [BLACK] - move king-side knight
        assertTrue(board1.play(6, 7, 5, 5));

        //  [WHITE] - move king-side knight
        assertTrue(board1.play(6, 0, 5, 2));

        //  [BLACK] - move previous knight
        assertTrue(board1.play(5, 5, 3, 4));

        //  [WHITE] - move knight
        assertTrue(board1.play(5, 2, 4, 4));

        //  [BLACK] - move knight
        assertTrue(board1.play(2, 5, 3, 3));

        //  [WHITE] - move knight
        assertTrue(board1.play(4, 4, 2, 5));

        //  [BLACK] - move knight
        assertTrue(board1.play(3, 4, 2, 2));

        //  [WHITE] - move knight, capture black knight
        assertTrue(board1.play(4, 3, 2, 2));

        //  [BLACK] - move knight, capture white knight
        assertTrue(board1.play(3, 3, 2, 5));

        //  [WHITE] - move knight
        assertTrue(board1.play(2, 2, 3, 4));

        //  [BLACK] - move knight
        assertTrue(board1.play(2, 5, 3, 3));

        //  [WHITE] - move knight, capture pawn
        assertTrue(board1.play(3, 4, 4, 6));

        //  [BLACK] - move knight, capture pawn
        assertTrue(board1.play(3, 3, 4, 1));

        //  [WHITE] - move knight, capture bishop
        assertTrue(board1.play(4, 6, 2, 7));

        //  [BLACK] - move knight, capture bishop
        assertTrue(board1.play(4, 1, 2, 0));

        //  [WHITE] - move knight, capture pawn
        assertTrue(board1.play(2, 7, 0, 6));

        //  [BLACK] - move knight, capture bishop
        assertTrue(board1.play(2, 0, 0, 1));
    }

    @Test
    public void testCastle() {
        // move king side horse
        assertTrue(board1.play(6, 0, 5, 2));
        // move black pawn
        assertTrue(board1.play(3, 6, 3, 4));
        // move white king pawn
        assertTrue(board1.play(4, 1, 4, 2));
        // move black pawn
        assertTrue(board1.play(4, 6, 4, 4));
        // move white bishop out of the way in order to castle
        assertTrue(board1.play(5, 0, 3, 2));
        // move black pawn again
        assertTrue(board1.play(3, 4, 3, 3));
        // castle
        assertTrue(board1.play(4, 0, 6, 0));
    }

    // King in Check exception unit test.
    @Test
    public void testInvalidCheckMove() {
        try {
            this.board1.play(3, 1, 3, 2);
            this.board1.play(4, 6, 4, 5);
            this.board1.play(0, 1, 0, 2);
            this.board1.play(3, 7, 7, 3);
            this.board1.play(0, 2, 0, 3);
            this.board1.play(5, 7, 1, 3);
            this.board1.play(3, 2, 3, 3);
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
            board1.play(4, 1, 4, 3);
            board1.play(2, 6, 2, 5);
            board1.play(3, 0, 7, 4);
            board1.play(0, 6, 0, 5);
            board1.play(5, 0, 2, 3);
            board1.play(0, 5, 0, 4);
            board1.play(7, 4, 5, 6);
            board1.play(0, 4, 0, 3);
        } catch (IllegalStateException e) {
            //TODO: make check for isGameOver() (is checkmate or stalemate)
        }
    }

    @Test
    public void testMoveQueenThroughPawn() {
        try {
            this.board1.play(4, 0, 4, 2);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    @Test
    public void testMoveBishopThroughPawn() {
        try {
            this.board1.play(2, 0, 4, 2);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move", e.getMessage());
        }
    }

    @Test
    public void testMoveKingThroughPawn() {
        try {
            this.board1.play(3, 0, 3, 1);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot move to square occupied by piece of same color", e.getMessage());
        }
    }

    @Test
    public void testMoveCastleThroughPawn() {
        try {
            this.board1.play(0, 0, 0, 2);
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
        board1.play(4, 1, 4, 3);
        board1.play(4, 6, 4, 4);
        board1.play(5, 0, 2, 3);
        board1.play(3, 6, 3, 5);
        board1.play(3, 0, 6, 3);
        board1.play(1, 7, 2, 5);
        board1.play(6, 3, 7, 4);
        board1.play(6, 7, 5, 5);
        board1.play(7, 4, 6, 4);
        board1.play(6, 6, 6, 5);
        board1.play(2, 3, 5, 6);
        board1.play(4, 7, 5, 6);
        board1.play(6, 4, 4, 2);
        board1.play(5, 5, 6, 3);
        board1.play(4, 2, 5, 2);
        board1.play(5, 6, 6, 6);
        board1.play(6, 0, 4, 1);
        board1.play(3, 5, 3, 4);
        board1.play(4, 3, 3, 4);
        board1.play(2, 5, 3, 3);
        board1.play(4, 1, 3, 3);
        board1.play(4, 4, 3, 3);
        board1.play(5, 2, 5, 3);
        board1.play(3, 3, 3, 2);
        board1.play(2, 1, 3, 2);
        board1.play(2, 7, 5, 4);
        board1.play(7, 1, 7, 2);
        board1.play(3, 7, 3, 4);
        board1.play(7, 2, 6, 3);
        board1.play(3, 4, 6, 1);
        board1.play(5, 3, 4, 4);
        board1.play(6, 6, 6, 7);
        board1.play(6, 3, 5, 4);
        board1.play(6, 1, 7, 0);
        board1.play(4, 0, 4, 1);
        board1.play(7, 0, 2, 0);
        board1.play(5, 4, 6, 5);
        board1.play(7, 6, 6, 5);
        board1.play(4, 4, 4, 5);
        board1.play(6, 7, 6, 6);
        board1.play(4, 5, 3, 6);
        board1.play(6, 6, 7, 5);
        board1.play(1, 0, 2, 2);
        board1.play(2, 0, 0, 0);
        board1.play(3, 6, 2, 6);
        board1.play(0, 7, 4, 7);
        board1.play(4, 1, 5, 2);
        board1.play(5, 7, 6, 6);
    }

    @Test
    public void testAllPossibleMoves() {
        // Test the moves available for white as a starting position.
        List<Move> moves = board1.getAllPossibleMoves();
        assertEquals(20, moves.size());

        // Make a move e4
        board1.play(4, 1, 4, 3);
        moves = board1.getAllPossibleMoves();
        // 4 knight moves, and 16 pawn moves
        assertEquals(20, moves.size());

        // black moves d4
        board1.play(4, 6, 4, 4);
        moves = board1.getAllPossibleMoves();
        // 14 possible pawn moves, 5 knight moves, 5 bishop moves, 4 queen moves, 1 king move.
        assertEquals(29, moves.size());

        // white moves f5 (king's gambit)
        board1.play(5, 1, 5, 3);
        System.out.println(this.board1.getBoard());
        moves = board1.getAllPossibleMoves();
        // black has 15 possible pawn moves, 5 knight moves, 5 bishop moves, 4 queen moves, 1 king move.
        assertEquals(30, moves.size());

        // black accepts king's gambit
        board1.play(4,4 ,5,3);
        moves = board1.getAllPossibleMoves();
        // white has 13 pawn moves, 5 knight moves, 5 bishop moves, 4 queen moves, 2 king moves
        assertEquals(29, moves.size());

        // BIZARRE MOVE, testing en passe
        board1.play(6, 1, 6,3);
        moves = board1.getAllPossibleMoves();
        System.out.println(moves.size());
        moves.forEach(move -> System.out.print(move + ", "));
        System.out.println(this.board1.getBoard());
    }
}