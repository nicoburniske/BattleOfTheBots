package com.burnyarosh.board.common;

import com.burnyarosh.board.Board;
import com.burnyarosh.board.Chess;
import com.burnyarosh.board.piece.*;

import java.util.List;

import static com.burnyarosh.board.Board.tryMove;

public final class NewMove {

    private final IPiece p;
    private final Chess.Color c;
    private final Coord origin;
    private final Coord target;
    private final String an;
    private final Type t;

    //	flags
    private final boolean isCapture;
    private final boolean isCheck;
    private final boolean isMate;

    public enum Type{
        STANDARD,
        CASTLE,
        EN_PASSANT,
        PROMOTION
    }

    public NewMove(Board b, Coord origin, Coord target, char promotion){

        this.p = b.getPieceAtCoord(origin);
        this.c =  b.getPieceAtCoord(origin).getIsBlack() ? Chess.Color.BLACK : Chess.Color.WHITE;
        this.target = target;
        this.origin = origin;
        this.t = classifyMove(b, origin, target);
        this.isCapture = b.getPieceAtCoord(target) != null && this.p.getIsBlack() != b.getPieceAtCoord(target).getIsBlack();
        this.isCheck = Chess.isInCheck(tryMove(b, origin, target), this.c);
        this.isMate = Chess.isMate(tryMove(b, origin, target),this.c);
        this.an = toAlgebraicNotation(b, promotion);
    }

    public Type getType(){
        return this.t;
    }

    public IPiece getPiece(){
        return this.p;
    }

    public String toString(){
        return this.an;
    }

    public static Type classifyMove(Board b, Coord origin, Coord target){
        if (b.getPieceAtCoord(origin) instanceof King && Math.abs(origin.getX() - target.getX()) == 2 && !Chess.isInDangerBetween(b, (b.getPieceAtCoord(origin).getIsBlack() ? Chess.Color.BLACK : Chess.Color.WHITE), origin, target)) {
            int direction = target.getX() - origin.getX();
            int fromCastleX = direction > 0 ? 7 : 0;
            int toCastleX = direction > 0 ? 5 : 3;
            if (Chess.isValidMoveBoolean(b, (b.getPieceAtCoord(origin).getIsBlack() ? Chess.Color.BLACK : Chess.Color.WHITE), new Coord(fromCastleX, target.getY()), new Coord(toCastleX, target.getY()))) {
                return Type.CASTLE;
            }
        } else if (b.getPieceAtCoord(origin) instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && b.getPieceAtCoord(origin) == null){
            List<Move> temp_history = b.getMoveHistory();
            if (origin.getY() == (b.getPieceAtCoord(origin).getIsBlack() ? 3 : 4) //  Condition #1
                    && temp_history.size() > 3  //  avoid OutOfBoundsException  (en passant impossible under 4 moves)
                    && temp_history.get(temp_history.size() - 2).getPiece() instanceof Pawn && (b.getPieceAtCoord(new Coord(target.getX(), origin.getY()))).getMoveCount() == 1){ //Conditions 2, 3, and 4
                return Type.EN_PASSANT;
            }
        } else if (b.getPieceAtCoord(origin) instanceof Pawn && target.getY() == (b.getPieceAtCoord(origin).getIsBlack() ? 0 : 7)){
            return Type.PROMOTION;
        }
        return Type.STANDARD;
    }

    private String toAlgebraicNotation(Board b, char promo){
        //  SPECIAL CONDITION: CASTLE
        if (this.t == Type.CASTLE){
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
                for (IPiece t : b.getPieces(this.c)){
                    if (t.toString().charAt(1) == this.p.toString().charAt(1)){
                        if (t.getPossibleMoves(b.getBoardArray(), b.getMoveHistory()).contains(this.target)){
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
        if (this.t == Type.PROMOTION){
            sb.append("=");
            sb.append((char) (promo-32));
        }
        if (this.isCheck){
            if (this.isMate){
                sb.append("#");
            } else {
                sb.append("+");
            }
        }
        return sb.toString();
    }


}
