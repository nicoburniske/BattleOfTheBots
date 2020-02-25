package com.burnyarosh.board.common;

import com.burnyarosh.board.piece.IPiece;
import com.burnyarosh.board.piece.King;
import com.burnyarosh.board.piece.Pawn;

public class Move {

    private IPiece p;
    private Coord origin;
    private Coord target;
    private boolean isCapture;
    private boolean isPromotion;
    private boolean isCastle;
    private boolean isEnPassant;
    private boolean isCheck;
    private boolean isCheckmate;


    public Move(){
    }

    public Move(IPiece[][] board, Coord origin, Coord target){
        this.p = board[origin.getX()][origin.getY()];
        this.target = target;
        this.origin = origin;
        this.isEnPassant = false;
        this.isCheck = false;
        this.isCheckmate = false;
        classifyMove(board);
    }

    public IPiece getPiece(){
        return this.p;
    }

    public Coord getOrigin(){
        return this.origin;
    }

    public Coord getTarget(){
        return this.target;
    }

    public void setIsCheck(){
        this.isCheck = true;
    }

    public void setIsCheckmate(){
        this.isCheckmate = true;
    }

    public void setEnPassant(){
        this.isEnPassant = true;
        this.isCapture = true;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (this.isCastle){
            if (target.getX() < origin.getX()){
                sb.append("0-0");
            } else {
                sb.append("0-0-0");
            }
        } else {
            if (!(this.p instanceof Pawn)){
                //  initial piece identifier
                sb.append(this.p.toString().charAt(1));
            }
            {   //  TODO: disambiguating moves
                // check for identical piece
                // check identical peices possible moves
                    // if move exists, must disambiguate
                //  add file of departure (if they differ)
                //  or - add rank of departure (if files are same)
                //  or (if the player has three or more identical pieces able to reach the same square (1+ promotions))
                    //  both file and rank
            }
            if (this.isCapture){
                if (this.p instanceof Pawn){
                    sb.append((char) (97 + this.origin.getX()));
                }
                sb.append("x");
            }
            {   //  Coordinate of destination square
                sb.append((char) (97 + this.target.getX()));
                sb.append(this.target.getY());
            }
            if (this.isPromotion){
                sb.append("=");
                //TODO: followed by the uppercase letter of the piece promoted to.
            }
            if (this.isEnPassant){
                sb.append("e.p.");
            }
            if (this.isCheck){
                if (this.isCheckmate){
                    sb.append("#");
                } else {
                    sb.append("+");
                }
            }
        }
        return sb.toString();
    }

    private void classifyMove(IPiece[][] board){
        this.isCapture = board[target.getX()][target.getY()] != null && this.p.getIsBlack() != board[target.getX()][target.getY()].getIsBlack();
        this.isCastle = this.p instanceof King && Math.abs(origin.getX() - target.getX()) == 2;
        this.isPromotion = this.p instanceof Pawn && target.getY() == (this.p.getIsBlack() ? 0 : 7);
    }

}
