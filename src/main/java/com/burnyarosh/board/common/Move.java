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

    public Move(Coord origin, IPiece p){
        this.p = p;
        this.target = new Coord(p.getX(), p.getY());
        this.origin = origin;
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

}
