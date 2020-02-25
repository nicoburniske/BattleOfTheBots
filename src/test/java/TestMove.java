import com.burnyarosh.board.ChessBoard;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMove {
    ChessBoard board1;

    @Before
    public void init() {
        board1 = new ChessBoard();
    }

    @Test
    public void testMoveToString_Series() {

        assertEquals("e4", moveAndExecute(board1, 4,1,4,3));

        //assertEquals("d6", moveAndExecute(board1,3,6,3,5));
        moveAndExecute(board1,3,6,3,5);

        assertEquals("Ne2", moveAndExecute(board1,6,0,4,1));

        assertEquals("Bf5", moveAndExecute(board1,2,7,5,4));

        assertEquals("Nbc3",moveAndExecute(board1,1,0,2,2));

        assertEquals("Na6",moveAndExecute(board1,1,7,0,5));

        assertEquals("Nd4",moveAndExecute(board1,4,1,3,3));

        assertEquals("Qd7",moveAndExecute(board1,3,7,3,6));

        assertEquals("Qh5",moveAndExecute(board1,3,0,7,4));

        assertEquals("O-O-O",moveAndExecute(board1,4,7,2,7));

        assertEquals("Bxa6",moveAndExecute(board1,5,0,0,5));

        assertEquals("Nf6",moveAndExecute(board1,6,7,5,5));

        assertEquals("Bxb7+",moveAndExecute(board1,0,5,1,6));

    }

    private static String moveAndExecute(ChessBoard b, int originX, int originY, int targetX, int targetY){
        //String an = new Move(b.getBoard(), new Coord(originX,originY), new Coord(targetX,targetY)).toString();
        b.playGame(originX, originY, targetX, targetY);
        return b.getHistory().get(b.getHistory().size()-1).toString();
    }
}
