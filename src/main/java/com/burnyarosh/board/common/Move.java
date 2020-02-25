package com.burnyarosh.board.common;

import com.burnyarosh.board.piece.IPiece;
import com.burnyarosh.board.piece.King;
import com.burnyarosh.board.piece.Pawn;

import java.util.ArrayList;
import java.util.List;

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
    private IPiece[][] board;
    private List<IPiece> teamPieces;


    public Move(){
    }

    public Move(IPiece[][] board, Coord origin, Coord target){
        this.p = board[origin.getX()][origin.getY()];
        this.target = target;
        this.origin = origin;
        this.isEnPassant = false;
        this.isCheck = false;
        this.isCheckmate = false;
        this.board = board;
        classifyMove();
        this.buildPieceList();
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
            if (target.getX() > origin.getX()){
                sb.append("O-O");
            } else {
                sb.append("O-O-O");
            }
        } else {
            if (!(this.p instanceof Pawn)){
                //  initial piece identifier
                sb.append(this.p.toString().charAt(1));
            }
            {   //  disambiguating moves
                if (!((this.p instanceof Pawn) || (this.p instanceof King))){
                    boolean requiresDisambiguation, sameFile, sameRank = false;
                    for ()
                }
                boolean sameFile = false;
                boolean sameRank = false;
                boolean sameMoveExists = false;
                for (IPiece t : this.teamPieces){
                    if (t.getClass() == this.p.getClass() ){
                        if (t.getPossibleMoves(this.board, null).contains(this.target)) {  //  TODO: THIS WILL BREAK WITH PAWNS
                            sameMoveExists = true;
                            if (t.getCoord().getX() == this.origin.getX()){
                                sameFile = true;
                            }
                            if (t.getCoord().getY() == this.origin.getY()){
                                sameRank = true;
                            }
                        }
                    }
                }
                if (sameMoveExists){
                    System.out.println("DB" +this.p);
                    if (!sameFile){
                        sb.append((char) (97 + this.origin.getX()));
                    } else if (sameFile && !sameRank){
                        sb.append(this.origin.getY()+1);
                    } else {
                        sb.append((char) (97 + this.origin.getX()));
                        sb.append(this.origin.getY() + 1);
                    }
                }
            }
            if (this.isCapture){
                if (this.p instanceof Pawn){
                    sb.append((char) (97 + this.origin.getX()));
                }
                sb.append("x");
            }
            {   //  Coordinate of destination square
                sb.append((char) (97 + this.target.getX()));
                sb.append(this.target.getY()+1);
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

    private void buildPieceList(){
        this.teamPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                IPiece curr = this.board[i][j];
                if(curr != null) {
                    if (curr.getIsBlack() == this.p.getIsBlack() && curr.getClass() == this.p.getClass()){
                        this.teamPieces.add(curr);
                    }
                }
            }
        }
        this.teamPieces.remove(this.p);
    }

    private void classifyMove(){
        this.isCapture = this.board[target.getX()][target.getY()] != null && this.p.getIsBlack() != this.board[target.getX()][target.getY()].getIsBlack();
        this.isCastle = this.p instanceof King && Math.abs(origin.getX() - target.getX()) == 2;
        this.isPromotion = this.p instanceof Pawn && target.getY() == (this.p.getIsBlack() ? 0 : 7);
    }

}
