import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestChessBoard2 {
    ChessBoard board1;

    @Before
    public void initExamples() {
        board1 = new ChessBoard();
    }

    @Test
    public void testBoardConstructor() {

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


}
