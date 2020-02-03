package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class Knight extends AbstractPiece {
    public Knight(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Knight(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    public IPiece copy() {
        return new Knight(super.getX(), super.getY(),super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return (Math.abs(toX - fromX) != 0 && Math.abs(toY - fromY) != 0 && Math.abs(toX - fromX) + Math.abs(toY - fromY) == 3);
    }

    /**
     *
     * If the given Coord is inside the board, and it is not moving to a position occupied by a piece of the same color
     * @param board
     * @return
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        Coord self = new Coord(super.getX(), super.getY());
        Coord[] skeleton = {
                new Coord(-1, 2), new Coord(1, 2),
                new Coord(2, -1), new Coord(2, 1),
                new Coord(-1, -2), new Coord(1, -2),
                new Coord(-2, -1), new Coord(-2, 1)
        };
        List<Coord> moves = new ArrayList<>();
        for (Coord c : skeleton){
            Coord temp = c.addCoords(self);
            if ( temp.isInsideBoard() && (super.getIsBlack() != board[temp.getX()][temp.getY()].getIsBlack()) ) moves.add(temp);
        }
        return moves;
    }

    @Override
    public String toString() {
        return super.toString() + "N";
    }


}
