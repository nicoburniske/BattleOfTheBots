package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends AbstractPiece {

    public Pawn(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Pawn(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    public IPiece copy() {
        return new Pawn(super.getX(), super.getY(),super.getIsBlack(), this.getIsFirstMove(), this.getMoveCount());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        int direction = super.getIsBlack() ? -1 : 1;
        IPiece to = board[toX][toY];
        if (!super.getIsFirstMove()) {
            return this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
        } else {
            return isFirstMoveValid(fromX, fromY, toX, toY, direction, to)
                    || this.isValidPawnMove(fromX, fromY, toX, toY, direction, to);
        }
    }

    /**
     * four possibilities:
     *  - pawn moves (directional) 1
     *  - pawn moves (directional) 2 (first move)
     *  - pawn moves (semi-directional (left)) 1 (capture possible) (en passent)
     *  - pawn moves (semi-directional (right)) 1 (capture possible) (en passent)
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        Coord self = new Coord(super.getX(), super.getY());
        Coord[] skeleton = {
                new Coord(0, (super.getIsBlack() ? -1 : 1)),
                new Coord(0, (super.getIsBlack() ? -2 : 2)),
                new Coord(-1, (super.getIsBlack() ? -1 : 1)),
                new Coord(1, (super.getIsBlack() ? -1 : 1))
        };
        List<Coord> moves = new ArrayList<>();
        for (Coord c : skeleton){
            Coord temp = c.addCoords(self);
            if (temp.isInsideBoard()){
                if (super.getIsFirstMove()){
                    if (this.isFirstMoveValid(self.getX(), self.getY(), temp.getX(), temp.getY(),(super.getIsBlack() ? -1 : 1), board[temp.getX()][temp.getY()])){
                        moves.add(temp);
                    }
                } else {
                    if (this.isValidPawnMove(self.getX(), self.getY(), temp.getX(), temp.getY(),(super.getIsBlack() ? -1 : 1), board[temp.getX()][temp.getY()])
                            || isValidEnPassant(self, temp, board, move_history)){
                        moves.add(temp);
                    }
                }
            }
        }
        return moves;
    }

    private boolean isValidEnPassant(Coord origin, Coord target, IPiece[][] board, List<Move> move_history){
        if ( board[origin.getX()][origin.getY()] instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && board[target.getX()][target.getY()] == null){ // std conditiona
            if ( (origin.getY() == (super.getIsBlack() ? 3 : 4)) ) {    //  Condition #1
                if (move_history.size() > 3) { //  avoid OutOfBoundsException  (en passant impossible under 4 moves)
                    return (move_history.get(move_history.size() - 2).getPiece() instanceof Pawn) && (board[target.getX()][origin.getY()].getMoveCount() == 1); //Conditions 2, 3, and 4
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "P";
    }

    private boolean isValidPawnMove(int fromX, int fromY, int toX, int toY, int direction, IPiece to) {
        return (fromX == toX && (fromY + direction == toY) && to == null)
                || ((Math.abs(fromX - toX) == 1) && (fromY + direction == toY) && to != null);
    }

    private boolean isFirstMoveValid(int fromX, int fromY, int toX, int toY, int direction, IPiece to) {
        return ((fromX == toX) && (toY == fromY + 2 * direction) && (to == null)) || isValidPawnMove(fromX, fromY, toX, toY, direction, to);
    }
}
