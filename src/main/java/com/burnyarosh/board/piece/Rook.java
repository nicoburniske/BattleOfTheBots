package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class Rook extends AbstractPiece {

    public Rook(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    public Rook(int x, int y, boolean isBlack, boolean isFirstMove, int moveCount) {
        super(x, y, isBlack, isFirstMove, moveCount);
    }

    public IPiece copy() {
        return new Rook(super.getX(), super.getY(), super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    @Override
    public boolean isValidMove(IPiece[][] board, int fromX, int fromY, int toX, int toY) {
        return super.validInlineMove(fromX, fromY, toX, toY)
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
                Coord tempUp = new Coord(self.getX(), i).addCoords(self);
                if ( tempUp.isInsideBoard() && (super.getIsBlack() != board[tempUp.getX()][tempUp.getY()].getIsBlack()) ){
                    moves.add(tempUp);
                    if ((super.getIsBlack() == !board[tempUp.getX()][tempUp.getY()].getIsBlack())){
                        upBlocked = true;
                    }
                } else {
                    upBlocked = true;
                }
            }
            if (!rightBlocked){
                Coord tempRight = new Coord(i, self.getY()).addCoords(self);
                if ( tempRight.isInsideBoard() && (super.getIsBlack() != board[tempRight.getX()][tempRight.getY()].getIsBlack()) ){
                    moves.add(tempRight);
                    if ((super.getIsBlack() == !board[tempRight.getX()][tempRight.getY()].getIsBlack())){
                        rightBlocked = true;
                    }
                } else {
                    rightBlocked = true;
                }
            }
            if (!downBlocked){
                Coord tempDown = new Coord(self.getX(), -i).addCoords(self);
                if ( tempDown.isInsideBoard() && (super.getIsBlack() != board[tempDown.getX()][tempDown.getY()].getIsBlack()) ){
                    moves.add(tempDown);
                    if ((super.getIsBlack() == !board[tempDown.getX()][tempDown.getY()].getIsBlack())){
                        downBlocked = true;
                    }
                } else {
                    downBlocked = true;
                }
            }
            if (!leftBlocked){
                Coord tempLeft = new Coord(-i, self.getY()).addCoords(self);
                if ( tempLeft.isInsideBoard() && (super.getIsBlack() != board[tempLeft.getX()][tempLeft.getY()].getIsBlack()) ){
                    moves.add(tempLeft);
                    if ((super.getIsBlack() == !board[tempLeft.getX()][tempLeft.getY()].getIsBlack())){
                        upBlocked = true;
                    }
                } else {
                    upBlocked = true;
                }
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return super.toString() + "C";
    }

}
