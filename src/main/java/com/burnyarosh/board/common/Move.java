package com.burnyarosh.board.common;

import com.burnyarosh.board.Board;
import com.burnyarosh.board.Chess;
import com.burnyarosh.board.piece.IPiece;
import com.burnyarosh.board.piece.King;
import com.burnyarosh.board.piece.Pawn;

import java.util.ArrayList;
import java.util.List;

public class Move {

    private IPiece p;
    private Coord origin;
    private Coord target;
    private Special type;
    private boolean isCapture;
    private boolean isPromotion; //
    private boolean isCastle; //
    private boolean isCheck;
    private boolean isCheckmate;
    private IPiece[][] board;
    private List<IPiece> teamPieces;
    private List<Move> history;
    private char promo;
    private String an;

    public enum Special
    {
        NONE,
        CASTLE,
        EN_PASSANT,
        PROMOTION
    }


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

    private Move(IPiece p, Coord origin, Coord target, boolean isCapture, boolean isPromotion, boolean isCastle, boolean isCheck, boolean isCheckmate,
                 IPiece[][] board, List<IPiece> teamPieces, List<Move> history, char promo){
        this.p = p;
        this.origin = origin;
        this.target = target;
        this.isCapture = isCapture;
        this.isPromotion = isPromotion;
        this.isCastle = isCastle;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.board = board;
        this.teamPieces = teamPieces;
        this.history = history;
        this.promo = promo;
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

    //  TODO: WHEN THIS METHOD IS CALLED THE MOVE IS FINALIZED AND WE CAN DELETE NONESSENTIAL MEMBERS
    private String toAlgebraicNotation(){
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
                        sb.append(this.origin.toChessString());
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
            sb.append(this.target.toChessString());
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

    public String toString(){
        if (this.an == null){
            this.an = this.toAlgebraicNotation();
        }
        return this.an;
    }

    public Move copy(){
        return new Move(this.p, this.origin, this.target, this.isCapture, this.isPromotion, this.isCastle, this.isCheck, this.isCheckmate, this.board, this.teamPieces, this.history, this.promo);
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

    //  TODO: UNFINISHED MAKE THIS PRIVATE AND WHATEVER MODIFY THE MOVE CLASS TO BE FINAL
    public static Special classifyMove(Board b, Coord origin, Coord target) {
        if (b.getPieceAtCoord(origin) instanceof King && Math.abs(origin.getX() - target.getX()) == 2 && !Chess.isInDangerBetween(b, (b.getPieceAtCoord(origin).getIsBlack() ? Chess.Color.BLACK : Chess.Color.WHITE), origin, target)) {
            int direction = target.getX() - origin.getX();
            int fromCastleX = direction > 0 ? 7 : 0;
            int toCastleX = direction > 0 ? 5 : 3;
            if (Chess.isValidMoveBoolean(b, (b.getPieceAtCoord(origin).getIsBlack() ? Chess.Color.BLACK : Chess.Color.WHITE), new Coord(fromCastleX, target.getY()), new Coord(toCastleX, target.getY()))) {
                return Special.CASTLE;
            }
        }
        if (b.getPieceAtCoord(origin) instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && b.getPieceAtCoord(origin) == null){
            List<Move> temp_history = b.getMoveHistory();
            if (origin.getY() == (b.getPieceAtCoord(origin).getIsBlack() ? 3 : 4) //  Condition #1
            && temp_history.size() > 3  //  avoid OutOfBoundsException  (en passant impossible under 4 moves)
            && temp_history.get(temp_history.size() - 2).getPiece() instanceof Pawn && (b.getPieceAtCoord(new Coord(target.getX(), origin.getY()))).getMoveCount() == 1){ //Conditions 2, 3, and 4
                return Special.EN_PASSANT;
            }
        }
        if (b.getPieceAtCoord(origin) instanceof Pawn && target.getY() == (b.getPieceAtCoord(origin).getIsBlack() ? 0 : 7)){
            return Special.PROMOTION;
        }
        return Special.NONE;
    }

    public Special getMoveType(){
        return this.type;
    }



}
