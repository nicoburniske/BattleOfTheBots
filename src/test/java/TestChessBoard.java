import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(true, board1.playGame(1, 0, 2, 2));

        //  [BLACK] - move queen-side knight
        assertEquals(true, board1.playGame(1, 7, 2, 5));

        //  [WHITE] - move previous knight
        assertEquals(true, board1.playGame(2, 2, 4, 3));

        //  [BLACK] - move king-side knight
        assertEquals(true, board1.playGame(6, 7, 5, 5));

        //  [WHITE] - move king-side knight
        assertEquals(true, board1.playGame(6, 0, 5, 2));

        //  [BLACK] - move previous knight
        assertEquals(true, board1.playGame(5, 5, 3, 4));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(5, 2, 4, 4));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(2, 5, 3, 3));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(4, 4, 2, 5));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(3, 3, 5, 2));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(4, 3, 5, 5));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(3, 4, 2, 2));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(2, 5, 1, 7));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(5, 2, 6, 0));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(5, 5, 6, 7));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(2, 2, 1, 0));

    }

    @Test
    public void testMoveKnights_Capture() {
        //  [WHITE] - move queen-side knight
        assertEquals(true, board1.playGame(1, 0, 2, 2));

        //  [BLACK] - move queen-side knight
        assertEquals(true, board1.playGame(1, 7, 2, 5));

        //  [WHITE] - move previous knight
        assertEquals(true, board1.playGame(2, 2, 4, 3));

        //  [BLACK] - move king-side knight
        assertEquals(true, board1.playGame(6, 7, 5, 5));

        //  [WHITE] - move king-side knight
        assertEquals(true, board1.playGame(6, 0, 5, 2));

        //  [BLACK] - move previous knight
        assertEquals(true, board1.playGame(5, 5, 3, 4));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(5, 2, 4, 4));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(2, 5, 3, 3));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(4, 4, 2, 5));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(3, 4, 2, 2));

        //  [WHITE] - move knight, capture black knight
        assertEquals(true, board1.playGame(4, 3, 2, 2));

        //  [BLACK] - move knight, capture white knight
        assertEquals(true, board1.playGame(3, 3, 2, 5));

        //  [WHITE] - move knight
        assertEquals(true, board1.playGame(2, 2, 3, 4));

        //  [BLACK] - move knight
        assertEquals(true, board1.playGame(2, 5, 3, 3));

        //  [WHITE] - move knight, capture pawn
        assertEquals(true, board1.playGame(3, 4, 4, 6));

        //  [BLACK] - move knight, capture pawn
        assertEquals(true, board1.playGame(3, 3, 4, 1));

        //  [WHITE] - move knight, capture bishop
        assertEquals(true, board1.playGame(4, 6, 2, 7));

        //  [BLACK] - move knight, capture bishop
        assertEquals(true, board1.playGame(4, 1, 2, 0));

        //  [WHITE] - move knight, capture pawn
        assertEquals(true, board1.playGame(2, 7, 0, 6));

        //  [BLACK] - move knight, capture bishop
        assertEquals(true, board1.playGame(2, 0, 0, 1));
    }

    @Test
    public void testMovePawns() {
        assertEquals(true, board1.isWhiteTurn());
        // move white King pawn
        assertEquals(true, board1.playGame(4, 1, 4, 3));

        assertEquals(false, board1.isWhiteTurn());
        // move black Queen pawn
        assertEquals(true, board1.playGame(3, 6, 3, 4));

        assertEquals(true, board1.isWhiteTurn());
        // move white king pawn again
        assertEquals(true, board1.playGame(4, 3, 4, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPawnStartMoveTwice() {
        // move white King pawn
        assertEquals(true, board1.playGame(4, 1, 4, 3));
        // move white King pawn twice again. Should result in IllegalArgumentException
        board1.playGame(4, 3, 4, 5);
    }

    @Test
    public void testCastle() {
        // move king side horse
        board1.playGame(6, 0, 5, 2);
        // move black pawn
        board1.playGame(3, 6, 3, 4);
        // move white king pawn
        board1.playGame(4, 1, 4, 2);
        // move black pawn
        board1.playGame(4, 6, 4, 4);
        // move white bishop out of the way in order to castle
        board1.playGame(5,0, 3, 2);
        // move black pawn again
        board1.playGame(3, 4, 3, 3);
        // castle
        board1.playGame(4, 0, 6, 0);
        System.out.println(board1);
    }
}
