package com.burnyarosh.board.common;

import com.burnyarosh.board.piece.IPiece;

public class Move {

    private IPiece p;
    private Coord origin;
    private Coord target;
    private boolean isCapture;
    private boolean isPromotion;
    private boolean isCastle;
    private boolean isCheck;
    private boolean isCheckmate;


    public Move(){
    }

    public Move(IPiece[][] board, Coord origin, Coord target){
        this.p = board[origin.getX()][origin.getY()];
        this.target = target;
        this.origin = origin;
        classifyMove(board);
    }

    public IPiece getPiece(){
        //TODO: Should this return a copy of IPiece p?
        return this.p;
    }

    public Coord getOrigin(){
        return this.origin;
    }

    public Coord getTarget(){
        return this.target;
    }

    public String toString(){
        //TODO: this
        return "return algebraic notation of move";
    }

    private void classifyMove(IPiece[][] board){
        if (board[target.getX()][target.getY()] != null){
            //TODO: modify this to account for En passant
            isCapture = true;
        } else {
            isCapture = false;
        }

    }

}
