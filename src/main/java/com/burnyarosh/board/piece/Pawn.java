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
        List<Coord> moves = new ArrayList<>();
        Coord temp1 = new Coord(self.getX(), (super.getIsBlack() ? -1 : 1)).addCoords(self);
        if (board[temp1.getX()][temp1.getY()] == null){
            moves.add(temp1);
        }
        //  move 2
        Coord temp2 = new Coord(self.getX(), (super.getIsBlack() ? -2 : 2)).addCoords(self);
        if (super.getIsFirstMove() && isFirstMoveValid(self.getX(), self.getY(), temp2.getX(), temp2.getY(), (super.getIsBlack() ? -1 : 1), board[temp2.getX()][temp2.getY()])){
            moves.add(temp2);
        }
        Coord temp3 = new Coord(-1, (super.getIsBlack() ? -1 : 1)).addCoords(self);
        if (temp3.isInsideBoard() && (this.isValidPawnMove(self.getX(), self.getY(), temp3.getX(), temp3.getY(), (super.getIsBlack() ? -1 : 1), board[temp3.getX()][temp3.getY()]) || isValidEnPassant(self, temp3, board, move_history))){
            moves.add(temp3);
        }
        Coord temp4 = new Coord(1, (super.getIsBlack() ? -1 : 1)).addCoords(self);
        if (temp4.isInsideBoard() && (this.isValidPawnMove(self.getX(), self.getY(), temp4.getX(), temp4.getY(), (super.getIsBlack() ? -1 : 1), board[temp4.getX()][temp4.getY()]) || isValidEnPassant(self, temp4, board, move_history))){
            moves.add(temp4);
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
        return (fromX == toX) && (toY == fromY + 2 * direction) && (to == null);
    }
}
