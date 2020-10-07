import com.burnyarosh.board.Chess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMove {
    private Chess board1;

    @BeforeEach
    public void init() {
        board1 = new Chess();
    }

    @Test
    public void testMoveToString_Series() {

        assertEquals("e4", moveAndExecute(board1, 4,1,4,3));

        assertEquals("d6", moveAndExecute(board1,3,6,3,5));

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

        assertEquals("Kxb7",moveAndExecute(board1,2,7,1,6));

        assertEquals("exf5",moveAndExecute(board1,4,3,5,4));

        assertEquals("e5",moveAndExecute(board1,4,6,4,4));

        assertEquals("fxe6",moveAndExecute(board1,5,4,4,5));    //  enpassant

        assertEquals("Qxe6+",moveAndExecute(board1,3,6,4,5));

        assertEquals("Nxe6",moveAndExecute(board1,3,3,4,5));

        assertEquals("Be7",moveAndExecute(board1,5,7,4,6));

        assertEquals("O-O",moveAndExecute(board1,4,0,6,0));

        assertEquals("Rde8",moveAndExecute(board1,3,7,4,7));

        assertEquals("d4",moveAndExecute(board1,3,1,3,3));

        assertEquals("fxe6",moveAndExecute(board1,5,6,4,5));

        assertEquals("Bf4",moveAndExecute(board1,2,0,5,3));

        assertEquals("Rb8",moveAndExecute(board1,4,7,1,7));

        assertEquals("Bxd6",moveAndExecute(board1,5,3,3,5));

        assertEquals("Ne8",moveAndExecute(board1,5,5,4,7));

        assertEquals("d5",moveAndExecute(board1,3,3,3,4));

        assertEquals("Bh4",moveAndExecute(board1,4,6,7,3));

        assertEquals("Bxc7",moveAndExecute(board1,3,5,2,6));

        assertEquals("Bxf2+",moveAndExecute(board1,7,3,5,1));

        assertEquals("Kxf2",moveAndExecute(board1,6,0,5,1));

        assertEquals("Nxc7",moveAndExecute(board1,4,7,2,6));

        assertEquals("d6",moveAndExecute(board1,3,4,3,5));

        assertEquals("Rhf8+",moveAndExecute(board1,7,7,5,7));

        assertEquals("Kg1",moveAndExecute(board1,5,1,6,0));

        assertEquals("Kb6",moveAndExecute(board1,1,6,1,5));

        assertEquals("d7",moveAndExecute(board1,3,5,3,6));

        assertEquals("e5",moveAndExecute(board1,4,5,4,4));

        assertEquals("d8=Q",moveAndExecute(board1,3,6,3,7));    //  PROMOTE TO QUEEN

        assertEquals("Kc5",moveAndExecute(board1,1,5,2,4));

        assertEquals("Qde8",moveAndExecute(board1,3,7,4,7));

        assertEquals("Nb5",moveAndExecute(board1,2,6,1,4));

        assertEquals("Qhxe5+",moveAndExecute(board1,7,4,4,4));

        assertEquals("Kb6",moveAndExecute(board1,2,4,1,5));

        assertEquals("Q8xb5#",moveAndExecute(board1,4,7,1,4));
    }

    private String moveAndExecute(Chess c, int originX, int originY, int targetX, int targetY){
        c.play(originX, originY, targetX, targetY);
        return c.getBoard().getMoveHistory().get(c.getBoard().getMoveHistory().size()-1).toString();
    }
}
