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
    public void testMovePawns() {
        // move white King pawn
        assertEquals(true, board1.playGame(4, 1, 4, 3));

        // move black Queen pawn
        assertEquals(true, board1.playGame(3, 6, 3, 4));

        // move white king pawn again
        assertEquals(true, board1.playGame(4, 3, 4, 4));

        System.out.println(board1.toString());
    }
}
