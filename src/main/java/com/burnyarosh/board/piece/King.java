package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends AbstractPiece {
    public King(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public King(int x, int y, boolean isBlack, boolean isFirstMove, int moveCount) {
        super(x, y, isBlack, isFirstMove, moveCount);
    }

    public IPiece copy() {
        return new King(super.getX(), super.getY(), super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        if (!super.getIsFirstMove()) {
            return isValidKingMove(board, fromX, fromY, toX, toY);
        } else {
            return isCastlingValid(board, fromX, fromY, toX, toY)
                    || this.isValidKingMove(board, fromX, fromY, toX, toY);
        }
    }

    /**
     * Possible Moves (King)
     *  - Coordinate on board (in-bounds)
     *  - Coordinate not occupied by piece of same color
     *
     * @param board
     * @return
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        Coord self = new Coord(super.getX(), super.getY());
        Coord[] skeleton = {
                new Coord(0, 1), new Coord(1, 1),
                new Coord(1, 0), new Coord(1, -1),
                new Coord(0, -1), new Coord(-1, -1),
                new Coord(-1, 0), new Coord(-1, 1)
        };
        List<Coord> moves = new ArrayList<>();
        for (Coord c : skeleton){
            Coord temp = c.addCoords(self);
            addValidMove(board, temp, moves);
        }
        return moves;
    }

    public String toString() {
        return super.toString() + "K";
    }

    private boolean isValidKingMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return (super.validDiagonalMove(fromX, fromY, toX, toY)
                || super.validInlineMove(fromX, fromY, toX, toY))
                && super.validLineMove(board, fromX, fromY, toX, toY, 1);
    }

    private boolean isCastlingValid(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        // checks if king is attempting to move 2 places to either side.
        // if the castle of the appropriate side hasn't moved.
        // if there is nothing in between.
        // TODO: check that there are no enemy pieces threatening the squares in between king and rook
        int direction = toX - super.getX();
        IPiece castle;

        if ((Math.abs(direction) == 2) && (super.getY() == toY)) {
            castle = board[direction < 0 ? super.getX() - 4 : super.getX() + 3][toY];
            if (castle == null) {
                return false;
            } else if (castle instanceof Rook && castle.getIsFirstMove()) {
                return super.notObstructed(board, super.getX(), super.getY(), castle.getX(), castle.getY());
            }
        }
        return false;
    }

}
