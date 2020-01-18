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
        assertEquals(true, board1.playGame(4, 1, 4, 3));
        assertEquals(true, board1.playGame(4, 6, 4, 4));
    }
}
