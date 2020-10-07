import com.burnyarosh.board.common.Coord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCoord {
    @Test
    public void testGetPointsInBetweenExclusive() {
        List<Coord> list = new Coord(0, 0).calculatePointsBetweenExclusive(new Coord(5, 5));
        assertEquals(4, list.size());
        for (int i = 1; i < 5; i++) {
            assertEquals(i, list.get(i - 1).getX());
            assertEquals(i, list.get(i - 1).getY());
        }
    }

    @Test
    public void testGetPointsInBetweenInclusiveEnd() {
        List<Coord> list = new Coord(0, 0).calculatePointsBetweenInclusiveEnd(new Coord(5, 5));
        assertEquals(5, list.size());
        for (int i = 1; i <= 5; i++) {
            assertEquals(i, list.get(i - 1).getX());
            assertEquals(i, list.get(i - 1).getY());
        }
    }
}
