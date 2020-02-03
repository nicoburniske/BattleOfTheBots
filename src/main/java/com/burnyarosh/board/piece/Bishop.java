package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends AbstractPiece {

    public Bishop(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Bishop(int x, int y, boolean isBlack, boolean firstMove, int moveCount) {
        super(x, y, isBlack, firstMove, moveCount);
    }

    public IPiece copy() {
        return new Bishop(super.getX(), super.getY(),super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validDiagonalMove(fromX, fromY, toX, toY)
                && super.validLineMove(board, fromX, fromY, toX, toY, 8);
    }

    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        Coord self = new Coord(super.getX(), super.getY());
        boolean upBlocked = false;
        boolean rightBlocked = false;
        boolean downBlocked = false;
        boolean leftBlocked = false;
        List<Coord> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++){
            if (!upBlocked){
                Coord tempUp = new Coord(i, i).addCoords(self);
                upBlocked = apm_inLineCheck(tempUp, board, moves);
            }
            if (!rightBlocked){
                Coord tempRight = new Coord(i, -i).addCoords(self);
                rightBlocked = apm_inLineCheck(tempRight, board, moves);
            }
            if (!downBlocked){
                Coord tempDown = new Coord(-i, -i).addCoords(self);
                downBlocked = apm_inLineCheck(tempDown, board, moves);
            }
            if (!leftBlocked){
                Coord tempLeft = new Coord(-i, i).addCoords(self);
                leftBlocked = apm_inLineCheck(tempLeft, board, moves);
            }
        }
        return moves;
    }

    private boolean apm_inLineCheck(Coord temp, IPiece[][] board, List<Coord> moves){
        if ( temp.isInsideBoard() && (super.getIsBlack() != board[temp.getX()][temp.getY()].getIsBlack()) ){
            moves.add(temp);
            if ((super.getIsBlack() == !board[temp.getX()][temp.getY()].getIsBlack())){
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public  String toString() {
        return super.toString() + "B";
    }
}
