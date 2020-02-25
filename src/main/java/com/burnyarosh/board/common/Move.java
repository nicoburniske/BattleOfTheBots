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
    private boolean isCheck;
    private boolean isCheckmate;
    private IPiece[][] board;
    private List<IPiece> teamPieces;
    private List<Move> history;
    private char promo;


    public Move(){
    }

    public Move(IPiece[][] board, List<Move> history, Coord origin, Coord target){
        this.p = board[origin.getX()][origin.getY()];
        this.target = target;
        this.origin = origin;
        this.isCheck = false;
        this.isCheckmate = false;
        this.board = board;
        this.history = history;
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
        this.isCapture = true;
    }

    public void setPromotionPiece(char p){
        this.promo = p;
    }

    public String toString(){

        //  SPECIAL CONDITION: CASTLE
        if (this.isCastle){
            if (target.getX() > origin.getX()){
                return "O-O";
            }
            return "O-O-O";
        }
        StringBuilder sb = new StringBuilder();
        if (!(this.p instanceof Pawn)){
            //  initial piece identifier
            sb.append(this.p.toString().charAt(1));
        }
        {   //  DISAMBIGUATING MOVES
            if (!((this.p instanceof Pawn) || (this.p instanceof King))){
                boolean requiresDisambiguation = false;
                boolean sameFile = false;
                boolean sameRank = false;
                for (IPiece t : this.teamPieces){
                    if (t.toString().charAt(1) == this.p.toString().charAt(1)){
                        if (t.getPossibleMoves(this.board, this.history).contains(this.target)){
                            requiresDisambiguation = true;
                            if (t.getCoord().getX() == this.origin.getX()){
                                sameFile = true;
                            }
                            if (t.getCoord().getY() == this.origin.getY()){
                                sameRank = true;
                            }
                        }
                    }
                }
                if (requiresDisambiguation){
                    if (!sameFile){
                        sb.append((char) (97 + this.origin.getX()));
                    } else if (!sameRank){
                        sb.append(this.origin.getY()+1);
                    } else {
                        sb.append((char) (97 + this.origin.getX()));
                        sb.append(this.origin.getY() + 1);
                    }
                }
            }
        }   //  END OF DISAMBIGUATING MOVES
        if (this.isCapture){
            if (this.p instanceof Pawn){
                sb.append((char) (97 + this.origin.getX()));
            }
            sb.append("x");
        }
        {   //  DESTINATION COORDINATES
            sb.append((char) (97 + this.target.getX()));
            sb.append(this.target.getY()+1);
        }   //  END OF DESTINATION COORDINATES
        if (this.isPromotion){
            sb.append("=");
            sb.append((char) (this.promo-32));
        }
        if (this.isCheck){
            if (this.isCheckmate){
                sb.append("#");
            } else {
                sb.append("+");
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
